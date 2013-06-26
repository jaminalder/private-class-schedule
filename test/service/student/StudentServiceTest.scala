package service.student

import org.specs2.mutable._
import play.api.test.WithServer
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._
import play.api.libs.ws.WS.WSRequestHolder
import play.api.libs.json.{Json, JsArray}
import crosscutting.transferobject.person.Person
import domain.person.PersonTestData
import crosscutting.transferobject.base.ImplicitJsonFormats._
import service.WiringModule.TeacherDomainComponent


class StudentServiceTest extends Specification {
  sequential

  "The StudentService" should {

    "add students of a teacher" in new WithServer {
      // clean up all existing persons and add a single teacher
      TeacherDomainComponent.dao.collection.drop
      TeacherDomainComponent.saveTeacher(PersonTestData.teacher)

      // add two students through the service
      val requestHolder = WS.url("http://localhost:19001/api/student/saveStudent")
      val response1: Response = await(requestHolder.post(Json.toJson(PersonTestData.randomStudentOfTeacher.person)))
      response1.status must equalTo(OK)
      val response2: Response = await(requestHolder.post(Json.toJson(PersonTestData.randomStudentOfTeacher.person)))
      response2.status must equalTo(OK)
    }


    "yield all students of a teacher" in new WithServer {

      // now call the other service to get the before added students
      val requestHolder: WSRequestHolder = WS.url("http://localhost:19001/api/student/allStudentsOfTeacher/" + PersonTestData.teacher.person.id._id)
      val response: Response = await(requestHolder.get)
      response.status must equalTo(OK)
      response.json.asInstanceOf[JsArray].value.size mustEqual 2
      val student1: Person = response.json(0).as[Person]
      val student2: Person = response.json(1).as[Person]
      student1.lastName.startsWith("last") must  beTrue
      student2.lastName.startsWith("last") must  beTrue
      student1.lastName mustNotEqual student2.lastName
    }
  }
}
