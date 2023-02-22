package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject

class TaskList2 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def login = Action { implicit request =>
    val userNameOption = request.session.get("username")
    userNameOption.map { username =>
      Ok("there is session")
    }.getOrElse(Ok("no session"))
  }
}
