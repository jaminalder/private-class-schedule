package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import models.{User, AppDB}
import slick.session.Session

class UserSpec extends Specification {

  /*

  "User" should {

    "be saved" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        AppDB.database.withSession {
          implicit session: Session =>
            AppDB.dal.Users.add(User(Some(2), "hola"))
            AppDB.dal.Users.countByName("pepe") must beEqualTo(0)
            AppDB.dal.Users.countByName("hola") must beEqualTo(1)

        }
      }
    }
  }

  */
}