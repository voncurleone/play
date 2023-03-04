package controllers

import models.MemoryModel
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request, Result}

import javax.inject.Inject

class TaskList3 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  case class UserData(username: String, password: String)
  case class TaskData(task: String, marked: Boolean)

  implicit val userDataReads: Reads[UserData] = Json.reads[UserData]
  implicit val taskDataReads: Reads[TaskData] = Json.reads[TaskData]

  private def withJson[A](f: A => Result)(implicit request: Request[AnyContent], reads: Reads[A]) = {
    request.body.asJson.map { body =>
      Json.fromJson(body)(reads) match {
        case JsSuccess(value, path) => f(value)
        case e @JsError(errors) => Redirect(routes.TaskList3.load)
      }
    }.getOrElse(Redirect(routes.TaskList3.load))
  }

  private def withSession(f: String => Result)(implicit request: Request[AnyContent]) = {
    request.session.get("username").map(f).getOrElse(Ok(Json.toJson(Seq(false))))
  }

  def load = Action { implicit request =>
    Ok(views.html.v3.main3())
  }

  def validateLogin = Action { implicit request =>
    withJson[UserData] { data =>
      if(MemoryModel.validateUser(data.username, data.password)) {
        Ok(Json.toJson(true))
          .withSession("username" -> data.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
      } else {
        Ok(Json.toJson(false))
      }
    }
  }

  def taskList = Action { implicit request =>
    withSession { username =>
      Ok(Json.toJson(MemoryModel.getTasks(username)))
    }
  }

  def logout = Action { implicit request =>
    Ok(Json.toJson(true)).withSession(request.session - "username")
  }

  def mark = Action { implicit request =>
    withSession { username =>
      withJson[Int] { data =>
        Ok(Json.toJson(MemoryModel.markTask(username, data)))
      }
    }
  }

  def remove = Action { implicit request =>
    withSession { username =>
      withJson[Int] { data =>
        Ok(Json.toJson(MemoryModel.removeTask(username, data)))
      }
    }
  }

  def createUser = Action { implicit request =>
    withJson[UserData]{ data =>
      val result = MemoryModel.createUser(data.username, data.password)

      if(result) {
        Ok(Json.toJson(true))
          .withSession("username" -> data.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
      } else {
        Ok(Json.toJson(false))
      }
    }
  }

  def addTask = Action { implicit request =>
    withSession { username =>
      withJson[TaskData] { data =>
        MemoryModel.addTask(username, data.task, data.marked)
        Ok(Json.toJson(true))
      }
    }
  }
}
