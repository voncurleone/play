package controllers

import models.MemoryModel
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject

class TaskList2 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def load = Action { implicit request =>
    val userNameOption = request.session.get("username")
    userNameOption.map { username =>
      Ok(views.html.main2(routes.TaskList2.taskForm.toString))
    }.getOrElse(Ok(views.html.main2(routes.TaskList2.login.toString)))
  }

  def login = Action { implicit request =>
    Ok(views.html.login2())
  }

  def validateLogin = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head

      if(MemoryModel.validateUser(username, password)) {
        Ok(views.html.taskForm())
          .withSession("username" -> username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
      } else {
        Ok(views.html.login2())
      }
    }.getOrElse(Ok(views.html.login2()))
  }

  def taskList = Action { implicit request =>
    val userNameOption = request.session.get("username")
    userNameOption.map { username =>
      Ok(views.html.taskList2(MemoryModel.getTasks(username)))
        //.withSession("username" -> username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
    }.getOrElse(Ok(views.html.login2()))
  }

  def taskForm = Action { implicit request =>
    val userNameOption = request.session.get("username")
    userNameOption.map { username =>
      Ok(views.html.taskForm())
    }.getOrElse(Ok(views.html.login2()))
  }

  def logOut = Action {
    Redirect(routes.TaskList2.load).withNewSession
  }

  def mark = Action { implicit request =>
    val userNameOption = request.session.get("username")
    userNameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val index = args("index").head.toInt
        MemoryModel.markTask(username, index)
        Ok(views.html.taskList2(MemoryModel.getTasks(username)))
      }.getOrElse(Ok(views.html.login2()))
    }.getOrElse(Ok(views.html.login2()))
  }

  def remove = Action { implicit request =>
    val userNameOption = request.session.get("username")
    userNameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val index = args("index").head.toInt
        MemoryModel.removeTask(username, index)
        Ok(views.html.taskList2(MemoryModel.getTasks(username)))
      }.getOrElse(Ok(views.html.login2()))
    }.getOrElse(Ok(views.html.login2()))
  }

  def add = Action { implicit request =>
    val userNameOption = request.session.get("username")
    userNameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val task = args("task").head
        val markedOption = args("marked").map(_.toBoolean).head
        MemoryModel.addTask(username, task, markedOption)
        Ok(views.html.taskList2(MemoryModel.getTasks(username)))
      }.getOrElse(Ok(views.html.login2()))
    }.getOrElse(Ok(views.html.login2()))
  }

  def createUser = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head

      if(MemoryModel.createUser(username, password)) {
        Ok(views.html.taskForm())
          .withSession("username" -> username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
      } else {
        Ok(views.html.login2())
      }
    }.getOrElse(Ok(views.html.login2()))
  }
}
