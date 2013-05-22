package dataaccess.person

import domain.person.{PersonDomainComponent}
import domain.role.{RoleDomainComponent}
import org.specs2.mutable._
import play.api.test.WithApplication
import crosscutting.basetype.Id
import dataaccess.role.{RoleDataConverterComponent, RoleDataAccessComponent}

object RoleComponent
  extends RoleDomainComponent
  with PersonDomainComponent
  with RoleDataAccessComponent
  with RoleDataConverterComponent {

  val dao = RoleDataAccessObject
  val dataObjectConverter = RoleDataObjectConverter

}

import RoleComponent._

object PersonTestData {

  val teacher = Teacher(Person(id=Id.generate, lastName="Meier", firstName="Hans", eMail="hans.meier@gmail.com",
    address=Address(street="street", streetNum = "3", city = "Bern", zip = "8000"), ownerID=Id.rootID))

  def randomStudentOfTeacher:Student = {
    val id = Id.generate
    val namePart = id._id.substring(3,6)
    Student(Person(id,"last"+namePart, "first"+namePart, namePart+"@server.com",
      Address(namePart+"street", namePart, namePart+"city", namePart), PersonTestData.teacher.person.id))
  }

}

class PersonDAOTest extends Specification {
  sequential

  "The PersonDAO" should {


    "Store a teacher" in new WithApplication {
      dao.collection.drop

      dao.persist(PersonTestData.teacher)
      success
    }

    "Retrieve a Person with Role by id" in new WithApplication {
      val storedTeacher: Option[Role] = dao.getById(PersonTestData.teacher.person.id)
      storedTeacher.get mustEqual PersonTestData.teacher
    }

    "Retrieve a Person with Role by email" in new WithApplication {
      val storedTeacher: Option[Role] = dao.getByEMail("hans.meier@gmail.com")
      storedTeacher.get mustEqual PersonTestData.teacher
    }

    "Store some Students for a Teacher" in new WithApplication {

      for (i <- 1 to 20) dao.persist(PersonTestData.randomStudentOfTeacher)
    }

    "Find all Students of a Teacher" in new WithApplication {
      val storedTeacher = dao.getById(PersonTestData.teacher.person.id).get.asInstanceOf[Teacher]
      val studentsOfTeacher = dao.getStudentsOfTeacher(storedTeacher)
      studentsOfTeacher.size mustEqual 20
    }

    "Delete some persons" in new WithApplication {
      val storedTeacher: Teacher = dao.getById(PersonTestData.teacher.person.id).get.asInstanceOf[Teacher]
      val studentsOfTeacher: List[Student] = dao.getStudentsOfTeacher(storedTeacher)
      studentsOfTeacher.foreach(student => dao.delete(student))
      dao.delete(storedTeacher)
      dao.getById(PersonTestData.teacher.person.id) must beNone
      dao.getStudentsOfTeacher(storedTeacher).size mustEqual 0
    }

  }


}