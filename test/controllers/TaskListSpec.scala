package controllers

import org.scalatestplus.play.{OneBrowserPerSuite, PlaySpec}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatestplus.play.HtmlUnitFactory

class TaskListSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {
  def login(): Unit = {
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

  def goToCreate(): Unit = {
    go to "http://localhost:9000"
    click on "create-user"
    find(cssSelector("h2")).isEmpty mustBe false
    find(cssSelector("h2")).foreach( _.text mustBe "create user")
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

      "add tasks" in {
        login()
        click on "input-task"
        textField("input-task").value = "unmarked"
        submit()
        eventually {
          findAll(cssSelector("li")).toList.map(_.text) mustBe List("unmarked", "item2 [marked]")
        }

        click on "input-task"
        textField("input-task").value = "marked"
        click on checkbox("input-marked")
        submit()
        eventually {
          findAll(cssSelector("li")).toList.map(_.text) mustBe List("marked [marked]", "unmarked", "item2 [marked]")
        }
      }
    }

    "createUser" should {
      "not create a user if input constraints arent met" in {
        goToCreate()
        click on "input-user"
        textField("input-user").value = "ma"
        click on "input-pass"
        pwdField("input-pass").value = "password"
        submit()
        eventually {
          find(cssSelector("h2")).foreach( _.text mustBe "create user")
        }

        click on "input-user"
        textField("input-user").value = "mammm"
        click on "input-pass"
        pwdField("input-pass").value = "p"
        submit()
        eventually {
          find(cssSelector("h2")).foreach( _.text mustBe "create user" )
        }
      }

      "not create a user where the username exists" in {
        goToCreate()
        click on "input-user"
        textField("input-user").value = "tim"
        click on "input-pass"
        pwdField("input-pass").value = "passswurd"
        submit()
        eventually {
          find(cssSelector("h2")).foreach( _.text mustBe "create user")
        }
      }

      "create a new user" in {
        goToCreate()
        click on "input-user"
        textField("input-user").value = "jim"
        click on "input-pass"
        pwdField("input-pass").value = "password"
        submit()
        eventually {
          find(cssSelector("h2")).foreach( _.text mustBe "Task List")
          findAll(cssSelector("li")).toList mustBe Nil
        }
      }
    }
  }
}
