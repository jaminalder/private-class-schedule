package service.authentication

import org.specs2.mutable._
import play.api.test.{WithApplication, WithServer}
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._
import play.api.libs.json.Json
import domain.person.{PersonTestData, TeacherDomainComponent}
import crosscutting.transferobject.base.ImplicitJsonFormats._
import play.api.mvc.{Cookies, Cookie, Result, Session}
import play.api.libs.ws.WS.WSRequestHolder
import crosscutting.transferobject.person.Person

class AuthenticationServiceTest extends Specification {
  sequential

  "The AuthenticationService" should {

    val rightPassword = "secret"

    "remove all persons firs" in new WithApplication {
      TeacherDomainComponent.dao.collection.drop
    }

    "register a user as teacher" in new WithServer {
      // prepare the json input
      val jsonUser = Json.toJson(PersonTestData.teacher.person)
      val jsonInput = Json.obj("user" -> jsonUser, "password" -> rightPassword)

      // call the service
      val requestHolder = WS.url("http://localhost:19001/api/authentication/registerUserAsTeacher")
      val response: Response = await(requestHolder.post(jsonInput))
      response.status must equalTo(OK)
      (response.json \ "eMail").as[String] mustEqual PersonTestData.teacher.person.eMail
      getValueFromSessonCookie(response, "userId") mustEqual Some(PersonTestData.teacher.id._id)
    }


    "log in a user as a teacher and get the logged in user" in new WithServer {
      val response: Response = testLoginUserAsTeacher(PersonTestData.teacher.person.eMail, rightPassword)
      response.status must equalTo(OK)
      (response.json \ "eMail").as[String] mustEqual PersonTestData.teacher.person.eMail
      getValueFromSessonCookie(response, "userId") mustEqual Some(PersonTestData.teacher.id._id)

      val loggedInUserResponse : Response = callServiceWithUserId("http://localhost:19001/api/authentication/getLoggedInUserAsTeacher", PersonTestData.teacher.id._id)
      loggedInUserResponse.status must equalTo(OK)
      loggedInUserResponse.json.as[Person] mustEqual PersonTestData.teacher.person
    }

    def getValueFromSessonCookie(response:Response, key:String): Option[String] = {
      val playSession: String = response.header("Set-Cookie").getOrElse(return None)
      val decodedCookies: Seq[Cookie] = Cookies.decode(playSession)
      val sessionCookie: Option[Cookie] = decodedCookies.find(c => c.name.equals("PLAY_SESSION"))
      val decodedSession: Session = Session.decodeFromCookie(sessionCookie)
      val valueInSession = decodedSession.get(key)
      return valueInSession
    }


    "fail to log in a user as a teacher with wrong password" in new WithServer {
      val response: Response = testLoginUserAsTeacher(PersonTestData.teacher.person.eMail, "blabla")
      response.status must equalTo(UNAUTHORIZED)
      getValueFromSessonCookie(response, "userId") must beNone
    }

    "fail to log in a user as a teacher with inexisting email" in new WithServer {
      val response: Response = testLoginUserAsTeacher("idonot@exist.com", rightPassword)
      response.status must equalTo(UNAUTHORIZED)
      getValueFromSessonCookie(response, "userId") must beNone
    }

    "ensure session is clean after a failed login" in new WithServer {
      val successfulResponse: Response = testLoginUserAsTeacher(PersonTestData.teacher.person.eMail, rightPassword)
      successfulResponse.status must equalTo(OK)
      getValueFromSessonCookie(successfulResponse, "userId") mustEqual Some(PersonTestData.teacher.id._id)

      val failedResponse: Response = testLoginUserAsTeacher(PersonTestData.teacher.person.eMail, "blabla")
      failedResponse.status must equalTo(UNAUTHORIZED)
      getValueFromSessonCookie(failedResponse, "userId") must beNone
    }

    def testLoginUserAsTeacher(eMail:String, password:String):Response = {
      val jsonInput = Json.obj("eMail" -> eMail, "password" -> password)
      val requestHolder = WS.url("http://localhost:19001/api/authentication/loginUserAsTeacher")
      await(requestHolder.post(jsonInput))
    }


    def callServiceWithUserId(url:String, userId:String): Response = {
      val sessionCookie: Cookie = Session.encodeAsCookie(Session(Map("userId" -> userId)))
      val loggedInUserRequestHolder: WSRequestHolder = WS.url(url)
        .withHeaders(play.api.http.HeaderNames.COOKIE -> Cookies.encode(Seq(sessionCookie)))
      return await(loggedInUserRequestHolder.get)

    }

  }
}
