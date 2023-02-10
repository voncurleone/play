package models

import scala.collection.mutable

object MemoryModel {
  private val users = mutable.Map[String, String]("tim" -> "password")
  //key: username, value: list[(task, marked)]
  private val tasks = mutable.Map[String, List[(String, Boolean)]]("tim" -> List(("item1", false), ("item2", false)))

  def validateUser(username: String, password: String): Boolean = {
    users.get(username).map(_ == password).getOrElse(false)
  }
  def createUser(username: String, password: String): Boolean = {
    if(users.contains(username)) false
    else {
      users(username) = password
      true
    }
  }

  def getTasks(username: String): List[(String, Boolean)] = {
    tasks.getOrElse(username, Nil)
  }

  def markTask(username: String, index: Int): Boolean = {
    if(index < 0 || index >= tasks(username).length || tasks(username).isEmpty) false
    else {
      val (task, marked) = tasks(username)(index)
      tasks(username) = tasks(username).patch(index, Seq((task, !marked)), 1)
      true
    }
  }

  def addTask(username: String, task: String): Unit = {
    tasks(username) = (task, false) :: tasks.get(username).getOrElse(Nil)
  }

  def removeTask(username: String, index: Int): Boolean = {
    if (index < 0 || index >= tasks(username).length || tasks(username).isEmpty) false
    else {
      tasks(username) = tasks(username).patch(index, Nil, 1)
      true
    }
  }
  
}
