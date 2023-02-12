package controllers

import javax.inject._
import play.api._
import play.api.data
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.MemoryModel

case class LoginData(username: String, password: String)

class TaskList @Inject()(val cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  private val loginForm: Form[LoginData] = Form(mapping(
    "username" -> text(4, 20),
    "password" -> text(5))(LoginData.apply)(LoginData.unapply)
  )

  def login = Action { implicit request => Ok(views.html.login(loginForm)) }

  def validateLogin = Action { implicit request =>
    val post = request.body.asFormUrlEncoded
    post map { args =>
      val username = args("username").head
      val password = args("password").head
      if(MemoryModel.validateUser(username, password)) {
        Redirect(routes.TaskList.taskList)
          .withSession("username" -> username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
      }
      else {
        Redirect(routes.TaskList.login).flashing("error" -> "invalid username or password")
      }
    }getOrElse(Redirect(routes.TaskList.login))
  }

  def createUser = Action { implicit request => ??? }
  def createUserForm = Action { implicit request => ??? }

  def taskList = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      Ok(views.html.taskList(MemoryModel.getTasks(username)))
    }.getOrElse(Ok(views.html.login(loginForm)))
  }

  def logOut = Action { implicit request => ??? }
  def addTask = Action {implicit request => ??? }

  def removeTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val index = args("index").head.toInt
        MemoryModel.removeTask(username, index)
        Ok(views.html.taskList(MemoryModel.getTasks(username)))
      }.getOrElse(Ok(views.html.login(loginForm)))
    }.getOrElse(Ok(views.html.login(loginForm)))
  }

  def markTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val index = args("index").head.toInt
        MemoryModel.markTask(username, index)
        Ok(views.html.taskList(MemoryModel.getTasks(username)))
      }.getOrElse(Ok(views.html.login(loginForm)))
    }.getOrElse(Ok(views.html.login(loginForm)))
  }
}
