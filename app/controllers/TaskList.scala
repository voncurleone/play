package controllers

import javax.inject._
import play.api._
import play.api.data
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.MemoryModel

case class LoginData(username: String, password: String)
case class TaskData(task: String, marked: Boolean)

class TaskList @Inject()(val cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  private val loginForm: Form[LoginData] = Form(mapping(
    "username" -> text(3, 20),
    "password" -> text(5))(LoginData.apply)(LoginData.unapply)
  )

  private val taskForm: Form[TaskData] = Form(mapping(
    "task" -> text,
    "marked" -> boolean
  )(TaskData.apply)(TaskData.unapply))

  def login = Action { implicit request => Ok(views.html.v1.login(loginForm)) }

  def validateLogin = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithError => BadRequest(views.html.v1.login(formWithError)),
      data => {
        if (MemoryModel.validateUser(data.username, data.password)) {
          Redirect(routes.TaskList.taskList)
            .withSession("username" -> data.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
        }
        else {
          Redirect(routes.TaskList.login).flashing("error" -> "invalid username or password")
        }
      }
    )
  }

  def createUser = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithError => BadRequest(views.html.v1.createUser(formWithError)),
      data => {
        if(MemoryModel.createUser(data.username, data.password)) {
          Redirect(routes.TaskList.taskList)
            .withSession("username" -> data.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
        } else Ok(views.html.v1.createUser(loginForm))
      }
    )
  }

  def createUserPage = Action { implicit request =>
    Ok(views.html.v1.createUser(loginForm))
  }

  def taskList = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      Ok(views.html.v1.taskList(MemoryModel.getTasks(username), taskForm))
    }.getOrElse(Ok(views.html.v1.login(loginForm)))
  }

  def logOut = Action { implicit request =>
    Redirect(routes.TaskList.taskList).withNewSession
  }

  def addTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val task = args("task").head
        val markedOption = args.get("marked").map(_.head.toBoolean)

        MemoryModel.addTask(username, task, markedOption.getOrElse(false))
        Ok(views.html.v1.taskList(MemoryModel.getTasks(username), taskForm))
      }.getOrElse(Ok(views.html.v1.login(loginForm)))
    }.getOrElse(Ok(views.html.v1.login(loginForm)))
  }

  def removeTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val index = args("index").head.toInt
        MemoryModel.removeTask(username, index)
        Ok(views.html.v1.taskList(MemoryModel.getTasks(username), taskForm))
      }.getOrElse(Ok(views.html.v1.login(loginForm)))
    }.getOrElse(Ok(views.html.v1.login(loginForm)))
  }

  def markTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val index = args("index").head.toInt
        MemoryModel.markTask(username, index)
        Ok(views.html.v1.taskList(MemoryModel.getTasks(username), taskForm))
      }.getOrElse(Ok(views.html.v1.login(loginForm)))
    }.getOrElse(Ok(views.html.v1.login(loginForm)))
  }
}
