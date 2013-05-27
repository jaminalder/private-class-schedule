package service.user

import org.specs2.mutable._
import play.api.test.WithServer
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._

class UserServiceTest extends Specification {
  sequential

  "UserService" should {

    "log in the dummy user" in new WithServer {
      val dummyUserMail = "dummy.user@email.com"
      val requestHolder = WS.url("http://localhost:19001/api/user/login/" + dummyUserMail + "/dummyPassword")
      val response: Response = await(requestHolder.get)
      response.status must equalTo(OK)
      (response.json \ "eMail").as[String] mustEqual dummyUserMail
    }

  }
}
