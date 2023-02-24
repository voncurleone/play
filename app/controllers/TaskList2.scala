package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject

class TaskList2 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def load = Action { implicit request =>
    val userNameOption = request.session.get("username")
    userNameOption.map { username =>
      Ok("there is session")
    }.getOrElse(Ok(views.html.main2(routes.TaskList2.login.toString)))
  }

  def login = Action { implicit request =>
    Ok(views.html.login2())
  }
}
