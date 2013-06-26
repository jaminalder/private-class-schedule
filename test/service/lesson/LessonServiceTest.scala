package service.lesson

import org.specs2.mutable._
import play.api.test.WithServer
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._
import play.api.libs.ws.WS.WSRequestHolder
import play.api.libs.json.{Json, JsArray}
import domain.person.PersonTestData
import crosscutting.transferobject.base.ImplicitJsonFormats._
import domain.lesson.LessonTestData
import crosscutting.transferobject.lesson.Lesson
import service.WiringModule.{TeacherDomainComponent, LessonDomainComponent}


class LessonServiceTest extends Specification {
  sequential

  "The LessonService" should {

    "add lesson of a teacher" in new WithServer {
      // clean up all existing teachers and add a single teacher
      TeacherDomainComponent.dao.collection.drop
      TeacherDomainComponent.saveTeacher(PersonTestData.teacher)

      // add two lessons through the service
      val requestHolder = WS.url("http://localhost:19001/api/lesson/saveLesson")
      val response1: Response = await(requestHolder.post(Json.toJson(LessonTestData.lesson1)))
      response1.status must equalTo(OK)
      val response2: Response = await(requestHolder.post(Json.toJson(LessonTestData.lesson2)))
      response2.status must equalTo(OK)

      val storedLesson1 = LessonDomainComponent.getLessonById(LessonTestData.lesson1Id)
      storedLesson1 mustEqual Some(LessonTestData.lesson1)

    }


    "yield all lessons of a teacher" in new WithServer {

      // now call the other service to get the before added lessons
      val requestHolder: WSRequestHolder = WS.url("http://localhost:19001/api/lesson/allLessonsOfTeacher/" + PersonTestData.teacher.person.id._id)
      val response: Response = await(requestHolder.get)
      response.status must equalTo(OK)
      response.json.asInstanceOf[JsArray].value.size mustEqual 2
      val lesson1: Lesson = response.json(0).as[Lesson]
      val lesson2: Lesson = response.json(1).as[Lesson]
      lesson1 mustEqual LessonTestData.lesson1
      lesson2 mustEqual LessonTestData.lesson2
    }
  }
}
