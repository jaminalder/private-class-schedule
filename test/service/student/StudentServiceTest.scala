package service.student

import org.specs2.mutable._
import play.api.test.{WithApplication, WithServer}
import play.api.test.Helpers._
import play.api.libs.json.Json
import domain.person.PersonTestData
import crosscutting.transferobject.base.ImplicitJsonFormats._
import service.WiringModule.{UserDomainComponent, TeacherDomainComponent}
import service.authentication.AuthenticationTestHelper._
import play.api.libs.json.JsArray
import crosscutting.transferobject.person.Person
import play.api.libs.ws.Response
import play.api.libs.ws.WS.WSRequestHolder

class StudentServiceTest extends Specification {
  sequential

  "The StudentService" should {

    val password = "secret"

    "cleanup persons and register a teacher as a user" in new WithApplication {
      TeacherDomainComponent.dao.collection.drop
      UserDomainComponent.registerUserAsTeacher(PersonTestData.teacher, password)
      success
    }

    "add students of a teacher" in new WithServer {
      val requestHolder = requestHolderWithUserId("http://localhost:19001/api/student/saveStudent", PersonTestData.teacher.id._id)
      val response1: Response = await(requestHolder.post(Json.toJson(PersonTestData.randomStudentOfTeacher.person)))
      response1.status must equalTo(OK)
      val response2: Response = await(requestHolder.post(Json.toJson(PersonTestData.randomStudentOfTeacher.person)))
      response2.status must equalTo(OK)
    }


    "yield all students of a teacher" in new WithServer {
      val requestHolder: WSRequestHolder = requestHolderWithUserId("http://localhost:19001/api/student/allStudentsOfTeacher/" + PersonTestData.teacher.person.id._id, PersonTestData.teacher.id._id)
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
