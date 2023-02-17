package controllers

import org.scalatestplus.play.{OneBrowserPerSuite, PlaySpec}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatestplus.play.HtmlUnitFactory

class TaskListSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {
  def login() = {
    go to "http://localhost:9000"
    pageTitle mustBe "Login"
    find(cssSelector("h2")).isEmpty mustBe false
    find(cssSelector("h2")).foreach(e => e.text mustBe "Login")
    click on "login-user"
    textField("login-user").value = "tim"
    click on "login-pass"
    pwdField("login-pass").value = "password"
    submit()
  }
  "TaskList" must {
    "login" should {
      "redirect to itself if login is incorrect" in {
        go to "http://localhost:9000"
        pageTitle mustBe "Login"
        find(cssSelector("h2")).isEmpty mustBe false
        find(cssSelector("h2")).foreach(e => e.text mustBe "Login")
        click on "login-user"
        textField("login-user").value = "dave"
        click on "login-pass"
        pwdField("login-pass").value = "password"
        submit()
        eventually {
          pageTitle mustBe "Login"
        }
      }

      "send user to tasklist after a successful login" in {
        go to "http://localhost:9000"
        pageTitle mustBe "Login"
        find(cssSelector("h2")).isEmpty mustBe false
        find(cssSelector("h2")).foreach(e => e.text mustBe "Login")
        click on "login-user"
        textField("login-user").value = "tim"
        click on "login-pass"
        pwdField("login-pass").value = "password"
        submit()
        eventually {
          pageTitle mustBe "Task List"
          find(cssSelector("h2")).isEmpty mustBe false
          find(cssSelector("h2")).foreach(e => e.text mustBe "Task List")
          findAll(cssSelector("li")).toList.map(_.text) mustBe List("item1 [marked]", "item2")
        }
      }
    }

    "task list" should {
      "logout when logout button is pressed" in {
        login()
        eventually {
          pageTitle mustBe "Task List"
          find(cssSelector("h2")).isEmpty mustBe false
          find(cssSelector("h2")).foreach(e => e.text mustBe "Task List")
          findAll(cssSelector("li")).toList.map(_.text) mustBe List("item1 [marked]", "item2")
          click on "logout-button"
        }
        eventually {
          pageTitle mustBe "Login"
          find(cssSelector("h2")).isEmpty mustBe false
          find(cssSelector("h2")).foreach(e => e.text mustBe "Login")
        }
      }

      "mark and unmark tasks" in {
        login()
        click on "mark-0"
        eventually {
          findAll(cssSelector("li")).toList.map(_.text) mustBe List("item1", "item2")
        }

        click on "mark-1"
        eventually {
          findAll(cssSelector("li")).toList.map(_.text) mustBe List("item1", "item2 [marked]")
        }
      }

      "remove tasks" in {
        login()
        click on "remove-0"
        eventually {
          findAll(cssSelector("li")).toList.map(_.text) mustBe List("item2 [marked]")
        }
      }
    }

    "add tasks" in {
      //both marked and unmarked
    }
  }
}
