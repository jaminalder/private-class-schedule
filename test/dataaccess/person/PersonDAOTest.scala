package dataaccess.person

import domain.person.{Address, Person}
import domain.role.{Role, Student, Teacher}
import org.specs2.mutable._
import play.api.test.WithApplication
import domain.base.ID

object PersonTestData {

  val teacher = Teacher(Person(_id=ID.generate, lastName="Meier", firstName="Hans", eMail="hans.meier@gmail.com",
    address=Address(street="street", streetNum = "3", city = "Bern", zip = "8000"), ownerID=ID.rootID))

  def randomStudentOfTeacher:Student = {
    val id = ID.generate
    val namePart = id.substring(3,6)
    Student(Person(id,"last"+namePart, "first"+namePart, namePart+"@server.com",
      Address(namePart+"street", namePart, namePart+"city", namePart), PersonTestData.teacher.person._id))
  }

}

class PersonDAOTest extends Specification {
  sequential

  "The PersonDAO" should {


    "Store a teacher" in new WithApplication {
      PersonDAO.collection.drop

      PersonDAO.persist(PersonTestData.teacher)
      success
    }

    "Retrieve a Person with Role by id" in new WithApplication {
      val storedTeacher: Option[Role] = PersonDAO.getByID(PersonTestData.teacher.person._id)
      storedTeacher.get mustEqual PersonTestData.teacher
    }

    "Retrieve a Person with Role by email" in new WithApplication {
      val storedTeacher: Option[Role] = PersonDAO.getByEMail("hans.meier@gmail.com")
      storedTeacher.get mustEqual PersonTestData.teacher
    }

    "Store some Students for a Teacher" in new WithApplication {

      for (i <- 1 to 20) PersonDAO.persist(PersonTestData.randomStudentOfTeacher)
    }

    "Find all Students of a Teacher" in new WithApplication {
      val storedTeacher = PersonDAO.getByID(PersonTestData.teacher.person._id).get.asInstanceOf[Teacher]
      val studentsOfTeacher = PersonDAO.getStudentsOfTeacher(storedTeacher)
      studentsOfTeacher.size mustEqual 20
    }

    "Delete some persons" in new WithApplication {
      val storedTeacher: Teacher = PersonDAO.getByID(PersonTestData.teacher.person._id).get.asInstanceOf[Teacher]
      val studentsOfTeacher: List[Student] = PersonDAO.getStudentsOfTeacher(storedTeacher)
      studentsOfTeacher.foreach(student => PersonDAO.delete(student))
      PersonDAO.delete(storedTeacher)
      PersonDAO.getByID(PersonTestData.teacher.person._id) must beNone
      PersonDAO.getStudentsOfTeacher(storedTeacher).size mustEqual 0
    }

  }


}