package dataaccess.person

import domain.person.{Address, Person}
import domain.role.{Student, Teacher}
import org.specs2.mutable._
import play.api.test.WithApplication
import domain.base.ID

class PersonDAOTest extends Specification {

  val teacherID = ID.generate

  "The PersonDAO" should {
    val teacher = Teacher(Person(_id=teacherID, lastName="Meier", firstName="Hans", eMail="hans.meier@gmail.com",
      address=Address(street="street", streetNum = "3", city = "Bern", zip = "8000"), ownerID=ID.rootID))

    "Store a teacher" in new WithApplication {
      PersonDAO.persist(teacher)
      success
    }

    "Retrieve a Person with Role by id" in new WithApplication {
      val storedTeacher = PersonDAO.getByID(teacherID)
      storedTeacher mustEqual teacher
    }

    "Store some Students for a Teacher" in new WithApplication {

      for (i <- 1 to 20) PersonDAO.persist(randomStudent)

      def randomStudent:Student = {
        val id = ID.generate
        val namePart = id.substring(3,6)
        Student(Person(id,"last"+namePart, "first"+namePart, namePart+"@server.com",
        Address(namePart+"street", namePart, namePart+"city", namePart), teacherID))
      }
    }

    "Find all Students of a Teacher" in {
      val storedTeacher = PersonDAO.getByID(teacherID).asInstanceOf[Teacher]
      val studentsOfTeacher = PersonDAO.getStudentsOfTeacher(storedTeacher)
      studentsOfTeacher.size mustEqual 20
    }
  }


}