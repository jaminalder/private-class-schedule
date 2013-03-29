package dataaccess.person

import domain.person.{Address, Person}
import domain.role.Teacher
import org.specs2.mutable._
import play.api.test.WithApplication

class PersonDAOTest extends Specification {

  "The PersonDAO" should {
    val teacher = Teacher(Person(lastName="Meier", firstName="Hans", eMail="hans.meier@gmail.com",
      address=Address(street="street", streetNum = "3", city = "Bern", zip = "8000")))

    "Store a teacher" in new WithApplication {
      PersonDAO.persist(teacher)
      success
    }

    "Retrieve a Person with Role by id" in new WithApplication {
      val storedTeacher = PersonDAO.getByID(teacher.person._id)
      storedTeacher mustEqual teacher
    }
  }


}