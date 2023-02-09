package models

import sun.security.util.Password

object MemoryModel {
  val users = Map[String, String]("tim" -> "password")
  //key: username, value: list[(task, marked)]
  val tasks = Map[String, List[(String, Boolean)]]("tim" -> List(("item1", false), ("item2", false)))

  def validateUser(username: String, password: String): Boolean = ???
  def createUser(username: String, password: String): Boolean = ???
  def getTasks(userName: String): List[(String, Boolean)] = ???
  def markTask(username: String, index: Int): Boolean = ???
  def addTask(username: String, task: String): Boolean = ???
  def removeTask(username: String, index: Int): Boolean = ???
  
}
