package service.student

import org.specs2.mutable._
import play.api.test.WithServer
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._
import dataaccess.person.{PersonTestData, PersonDAO}
import play.api.libs.ws.WS.WSRequestHolder
import conversion.json.PersonJsonConverter._
import domain.person.Person
import play.api.libs.json.{Json, JsArray, JsValue}

class StudentServiceTest extends Specification {
  sequential

  "StudentService" should {

    "add students of a teacher" in new WithServer {
      // clean up all existing persons and add a single teacher
      PersonDAO.collection.drop
      PersonDAO.persist(PersonTestData.teacher)

      // add two students through the service
      val requestHolder = WS.url("http://localhost:19001/api/student/saveStudent")
      val response1: Response = await(requestHolder.post(PersonTestData.randomStudentOfTeacher.person.toJson))
      response1.status must equalTo(OK)
      val response2: Response = await(requestHolder.post(PersonTestData.randomStudentOfTeacher.person.toJson))
      response2.status must equalTo(OK)
    }


    "yield all students of a teacher" in new WithServer {

      // now call the other service to get the before added students
      val requestHolder: WSRequestHolder = WS.url("http://localhost:19001/api/student/allStudentsOfTeacher/" + PersonTestData.teacher.person._id)
      val response: Response = await(requestHolder.get)
      response.status must equalTo(OK)
      response.json.asInstanceOf[JsArray].value.size mustEqual 2
      val student1: Person = toPerson(response.json(0))
      val student2: Person = toPerson(response.json(1))
      student1.lastName.startsWith("last") must  beTrue
      student2.lastName.startsWith("last") must  beTrue
      student1.lastName mustNotEqual student2.lastName
    }
  }
}
