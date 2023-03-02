package controllers

import models.MemoryModel
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request, Result}

import javax.inject.Inject

class TaskList3 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  case class UserData(username: String, password: String)

  def withJson[A](f: A => Result)(implicit request: Request[AnyContent], reads: Reads[A]) = {
    request.body.asJson.map { body =>
      Json.fromJson(body) match {
        case JsSuccess(value, path) => f(value)
        case e @JsError(errors) => Redirect(routes.TaskList3.load)
      }
    }.getOrElse(Redirect(routes.TaskList3.load))
  }

  def load = Action { implicit request =>
    Ok(views.html.v3.main3())
  }

  def validateLogin = Action { implicit request =>
    implicit val userDataReads = Json.reads[UserData]
    withJson[UserData] { data =>
      if(MemoryModel.validateUser(data.username, data.password)) {
        Ok(Json.toJson(true))
          .withSession("usernaame" -> data.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
      } else {
        Ok(Json.toJson(false))
      }
    }
  }
}
