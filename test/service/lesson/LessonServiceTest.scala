package service.lesson

import org.specs2.mutable._
import play.api.test.{WithApplication, WithServer}
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._
import play.api.libs.ws.WS.WSRequestHolder
import play.api.libs.json.{Json, JsArray}
import domain.person.PersonTestData
import crosscutting.transferobject.base.ImplicitJsonFormats._
import domain.lesson.LessonTestData
import crosscutting.transferobject.lesson.Lesson
import service.WiringModule.{UserDomainComponent, TeacherDomainComponent, LessonDomainComponent}
import service.authentication.AuthenticationTestHelper._
import play.api.libs.json.JsArray
import crosscutting.transferobject.lesson.Lesson
import play.api.libs.ws.Response
import play.api.libs.ws.WS.WSRequestHolder
import scala.Some
import crosscutting.basetype.Id


class LessonServiceTest extends Specification {
  sequential

  "The LessonService" should {

    val password = "secret"

    "cleanup persons and register a teacher as a user" in new WithApplication {
      TeacherDomainComponent.dao.collection.drop
      UserDomainComponent.registerUserAsTeacher(PersonTestData.teacher, password)
      success
    }

    "add lesson of a teacher" in new WithServer {
      val requestHolder = requestHolderWithUserId("http://localhost:19001/api/lesson/saveLesson", PersonTestData.teacher.id._id)
      val response1: Response = await(requestHolder.post(Json.toJson(LessonTestData.lesson1)))
      response1.status must equalTo(OK)
      val response2: Response = await(requestHolder.post(Json.toJson(LessonTestData.lesson2)))
      response2.status must equalTo(OK)

      val storedLesson1 = LessonDomainComponent.getLessonById(LessonTestData.lesson1Id)
      storedLesson1 mustEqual Some(LessonTestData.lesson1)

      val storedLesson2 = LessonDomainComponent.getLessonById(LessonTestData.lesson2Id)
      storedLesson2 mustEqual Some(LessonTestData.lesson2)
    }

    "fail to add lesson for a teacher which is not the logged in user" in new WithServer {
      val requestHolder = requestHolderWithUserId("http://localhost:19001/api/lesson/saveLesson", Id.generate._id)
      val response = await(requestHolder.post(Json.toJson(LessonTestData.lesson3)))
      response.status must equalTo(FORBIDDEN)

      val storedLesson3 = LessonDomainComponent.getLessonById(LessonTestData.lesson3Id)
      storedLesson3 mustEqual None
    }


    "yield all lessons of a teacher" in new WithServer {

      // now call the other service to get the before added lessons
      val requestHolder = requestHolderWithUserId("http://localhost:19001/api/lesson/allLessonsOfTeacher/" + PersonTestData.teacher.person.id._id, PersonTestData.teacher.id._id)
      val response: Response = await(requestHolder.get)
      response.status must equalTo(OK)
      response.json.asInstanceOf[JsArray].value.size mustEqual 2
      val lesson1: Lesson = response.json(0).as[Lesson]
      val lesson2: Lesson = response.json(1).as[Lesson]
      lesson1 mustEqual LessonTestData.lesson1
      lesson2 mustEqual LessonTestData.lesson2
    }
  }

  "fail to yeald lessons of a teacher which is not the logged in user" in new WithServer {

    val requestHolder = requestHolderWithUserId("http://localhost:19001/api/lesson/allLessonsOfTeacher/" + PersonTestData.teacher.person.id._id, Id.generate._id)
    val response: Response = await(requestHolder.get)
    response.status must equalTo(FORBIDDEN)
  }

  "delete lesson of a teacher" in new WithServer {
    val requestHolder = requestHolderWithUserId("http://localhost:19001/api/lesson/deleteLesson", PersonTestData.teacher.id._id)
    val response1: Response = await(requestHolder.post(Json.toJson(LessonTestData.lesson1)))
    response1.status must equalTo(OK)

    val storedLesson1 = LessonDomainComponent.getLessonById(LessonTestData.lesson1Id)
    storedLesson1 mustEqual None

    val storedLesson2 = LessonDomainComponent.getLessonById(LessonTestData.lesson2Id)
    storedLesson2 mustEqual Some(LessonTestData.lesson2)
  }

  "fail to delete a lesson for a teacher which is not the logged in user" in new WithServer {
    val requestHolder = requestHolderWithUserId("http://localhost:19001/api/lesson/deleteLesson", Id.generate._id)
    val response = await(requestHolder.post(Json.toJson(LessonTestData.lesson2)))
    response.status must equalTo(FORBIDDEN)

    // ensure that the lesson is still in the db
    val storedLesson2 = LessonDomainComponent.getLessonById(LessonTestData.lesson2Id)
    storedLesson2 mustEqual Some(LessonTestData.lesson2)
  }


}
