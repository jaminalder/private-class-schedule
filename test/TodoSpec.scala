package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import models.{ToDo, AppDB}
import slick.session.Session
import java.util.UUID

class TodoSpec extends Specification {

  "Todo" should {

    "be saved" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        AppDB.database.withSession {
          implicit session: Session =>
            AppDB.dal.ToDos.add(ToDo(Some(UUID.randomUUID().toString()), "blabla", true))
            AppDB.dal.ToDos.countByText("blabla") must beEqualTo(1)
            AppDB.dal.ToDos.countByText("trallalla") must beEqualTo(0)
        }
      }
    }
  }

}