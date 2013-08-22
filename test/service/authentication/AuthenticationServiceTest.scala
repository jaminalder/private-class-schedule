package service.authentication

import org.specs2.mutable._
import play.api.test.{WithApplication, WithServer}
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._
import play.api.libs.json.Json
import domain.person.PersonTestData
import crosscutting.transferobject.base.ImplicitJsonFormats._
import crosscutting.transferobject.person.Person
import service.WiringModule.TeacherDomainComponent
import AuthenticationTestHelper._

class AuthenticationServiceTest extends Specification {
  sequential

  "The AuthenticationService" should {

    val rightPassword = "secret"

    "remove all persons first" in new WithApplication {
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

    "logoff a user and ensure cleaned sesson" in new WithServer {
      // login first
      val successfulResponse: Response = testLoginUserAsTeacher(PersonTestData.teacher.person.eMail, rightPassword)
      successfulResponse.status must equalTo(OK)
      getValueFromSessonCookie(successfulResponse, "userId") mustEqual Some(PersonTestData.teacher.id._id)

      // logoff
      val requestHolder = WS.url("http://localhost:19001/api/authentication/logoutUser")
      val response: Response = await(requestHolder.post(""))
      response.status must equalTo(OK)
      getValueFromSessonCookie(response, "userId") must beNone
    }

  }

}
