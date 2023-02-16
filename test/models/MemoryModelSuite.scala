package models

import org.scalatestplus.play._

class MemoryModelSuite extends PlaySpec {
  "Memory model" must {
    "validateUser" should {
      "return true if the user and password are valid" in {
        MemoryModel.validateUser("tim", "password") mustBe (true)
      }

      "return false if the wrong password is given" in {
        MemoryModel.validateUser("tim", "passs") mustBe (false)
      }

      "return false if the username is not valid" in {
        MemoryModel.validateUser("timm", "password") mustBe (false)
      }
    }

    "createUser" should {
      "return true if the username is not taken" in {
        MemoryModel.createUser("dave", "pass") mustBe (true)
      }

      "return false if the username is taken" in {
        MemoryModel.createUser("tim", "passsss") mustBe (false)
      }

      "be able to validate new user" in {
        MemoryModel.validateUser("dave", "pass") mustBe (true)
      }
    }

    "getTasks" should {
      "return the correct tasks for the default user" in {
        val expected = List(("item1", true), ("item2", false))

        MemoryModel.getTasks("tim") mustBe (expected)
      }

      "return Nil for a newly created user" in {
        MemoryModel.createUser("dave", "pass") mustBe (true)
        MemoryModel.getTasks("dave") mustBe (Nil)
      }
    }

    "markTask" should {
      "return false if index is out of bounds" in {
        MemoryModel.markTask("tim", 3) mustBe (false)
      }

      "return false if the user has no tasks" in {
        MemoryModel.createUser("dave", "pass") mustBe (true)
        MemoryModel.markTask("dave", 0) mustBe (false)
      }

      "return false if the index is negative" in {
        MemoryModel.markTask("tim", -2) mustBe (false)
      }

      "return true and mark the correct task" in {
        MemoryModel.markTask("tim", 1) mustBe (true)
        val expected = List(("item1", true), ("item2", true))

        MemoryModel.getTasks("tim") mustBe (expected)
      }
    }

    "addTask" should {
      "add a task to the front of the list" in {
        val expected = List(("task", false), ("item1", true), ("item2", false))

        MemoryModel.addTask("tim", "task", false)
        MemoryModel.getTasks("tim") mustBe (expected)
      }
    }

    "removeTask" should {
      "return false if index is negative" in {
        MemoryModel.removeTask("tim", -1) mustBe (false)
      }

      "return false if index is greater than or equal to length" in {
        MemoryModel.removeTask("tim", 3) mustBe (false)
      }

      "return false if list is empty" in {
        MemoryModel.createUser("dave", "pass") mustBe (true)
        MemoryModel.removeTask("dave", 0) mustBe (false)
      }

      "remove the correct task from the list" in {
        val expected = List(("item1", true))

        MemoryModel.removeTask("tim", 1) mustBe (true)
        MemoryModel.getTasks("tim") mustBe (expected)
      }
    }
  }
}
