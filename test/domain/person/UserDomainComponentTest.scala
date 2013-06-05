package domain.person

import org.specs2.mutable._
import play.api.test.WithApplication
import crosscutting.transferobject.person.Teacher

class UserDomainComponentTest extends Specification {

  sequential

  "The UserDomainComponent" should {

    val password = "secret"

    "remove all persons first" in new WithApplication {
      TeacherDomainComponent.dao.collection.drop
      success
    }

    "register a user as a teacher" in new WithApplication {
      UserDomainComponent.registerUserAsTeacher(PersonTestData.teacher, password)
      success
    }

    "successfully authenticate a user as a teacher" in new WithApplication {
      UserDomainComponent.authenticateUserAsTeacher(PersonTestData.teacher.person.eMail, password) match {
        case Some(teacher: Teacher) => teacher mustEqual PersonTestData.teacher
        case _ => failure
      }

    }

    "fail to authenticate a user if a wrong password is given" in new WithApplication {
      UserDomainComponent.authenticateUserAsTeacher(PersonTestData.teacher.person.eMail, "wrongPassword") match {
        case None => success
        case Some(teacher) => failure
        case _ => failure
      }
    }
  }
}
