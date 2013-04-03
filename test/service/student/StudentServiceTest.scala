package service.student

import org.specs2.mutable._
import play.api.test.WithServer
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._

object StudentServiceTest extends Specification {

  "StudentService" should {
    "provide all students of a teacher" in new WithServer {
      val response: Response = await(WS.url("http://localhost:19001/api/student/allForTeacher/6161efc1-05af-4704-8a7a-4a16f102020e").get)
      response.status must equalTo(OK)
      println("last name of first element: " + response.json(1) \ "lastName")
    }
  }
}
