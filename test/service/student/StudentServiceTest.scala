package service.student

import org.specs2.mutable._
import play.api.test.{WithApplication, WithServer}
import play.api.test.Helpers._
import play.api.libs.json.Json
import domain.person.PersonTestData
import crosscutting.transferobject.base.ImplicitJsonFormats._
import service.WiringModule.{StudentDomainComponent, UserDomainComponent, TeacherDomainComponent}
import service.authentication.AuthenticationTestHelper._
import play.api.libs.json.JsArray
import crosscutting.transferobject.person.{Student, Person}
import play.api.libs.ws.Response
import play.api.libs.ws.WS.WSRequestHolder
import crosscutting.basetype.Id

class StudentServiceTest extends Specification {
  sequential

  "The StudentService" should {

    val password = "secret"

    var student1: Person = null
    var student2: Person = null

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

    "fail to add students of a teacher which is not the logged in user" in new WithServer {
      val requestHolder = requestHolderWithUserId("http://localhost:19001/api/student/saveStudent", Id.generate._id)
      val response1: Response = await(requestHolder.post(Json.toJson(PersonTestData.randomStudentOfTeacher.person)))
      response1.status must equalTo(FORBIDDEN)
    }

    "yield all students of a teacher" in new WithServer {
      val requestHolder: WSRequestHolder = requestHolderWithUserId("http://localhost:19001/api/student/allStudentsOfTeacher/" + PersonTestData.teacher.person.id._id, PersonTestData.teacher.id._id)
      val response: Response = await(requestHolder.get)
      response.status must equalTo(OK)
      response.json.asInstanceOf[JsArray].value.size mustEqual 2
      student1 = response.json(0).as[Person]
      student2 = response.json(1).as[Person]
      student1.lastName.startsWith("last") must  beTrue
      student2.lastName.startsWith("last") must  beTrue
      student1.lastName mustNotEqual student2.lastName
    }

    "fail to yield students of a teacher which is not the logged in user" in new WithServer {
      val requestHolder: WSRequestHolder = requestHolderWithUserId("http://localhost:19001/api/student/allStudentsOfTeacher/" + PersonTestData.teacher.person.id._id, Id.generate._id)
      val response: Response = await(requestHolder.get)
      response.status must equalTo(FORBIDDEN)
    }

    "delete students of a teacher" in new WithServer {
      val storedStudent1: Option[Student] = StudentDomainComponent.getStudentById(student1.id)
      storedStudent1 mustEqual Some(Student(student1))
      val requestHolder = requestHolderWithUserId("http://localhost:19001/api/student/deleteStudent", PersonTestData.teacher.id._id)
      val response: Response = await(requestHolder.post(Json.toJson(student1)))
      response.status must equalTo(OK)
      val deletedStudent1: Option[Student] = StudentDomainComponent.getStudentById(student1.id)
      deletedStudent1 mustEqual None
    }

    "fail to delete students of a teacher which is not the logged in user" in new WithServer {
      val storedStudent2: Option[Student] = StudentDomainComponent.getStudentById(student2.id)
      storedStudent2 mustEqual Some(Student(student2))
      val requestHolder = requestHolderWithUserId("http://localhost:19001/api/student/deleteStudent", Id.generate._id)
      val response: Response = await(requestHolder.post(Json.toJson(student2)))
      response.status must equalTo(FORBIDDEN)
      val notDeletedStudent2: Option[Student] = StudentDomainComponent.getStudentById(student2.id)
      notDeletedStudent2 mustEqual Some(Student(student2))
    }


  }
}
