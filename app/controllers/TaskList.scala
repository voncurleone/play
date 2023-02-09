package controllers

import javax.inject._
import play.api._
import play.api.mvc._

class TaskList @Inject()(val cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  def login = Action { implicit request => ??? }
  def validateLogin = Action { implicit request => ??? }
  def createUser = Action { implicit request => ??? }
  def createUserForm = Action { implicit request => ??? }
  def taskList = Action { implicit request => ??? }
  def logOut = Action { implicit request => ??? }
  def addTask = Action {implicit request => ??? }
  def removeTask = Action { implicit request => ??? }
  def toggleTask = Action { implicit request => ???}
}
