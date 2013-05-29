package domain.person

import org.specs2.mutable._
import play.api.test.WithApplication
import crosscutting.transferobject.person.Teacher

class TeacherDomainComponentTest extends Specification {

  sequential

  "A Teacher" should {

    "remove all persons first" in new WithApplication {
      TeacherDomainComponent.dao.collection.drop
      success
    }

    "be storable and retrievable by id" in new WithApplication {
      TeacherDomainComponent.saveTeacher(PersonTestData.teacher)
      val storedTeacher: Option[Teacher] = TeacherDomainComponent.getTeacherById(PersonTestData.teacher.person.id)
      storedTeacher.get mustEqual PersonTestData.teacher
    }

    "be retrievable by email" in new WithApplication {
      val storedTeacher: Option[Teacher] = TeacherDomainComponent.getTeacherByEmail("hans.meier@gmail.com")
      storedTeacher.get mustEqual PersonTestData.teacher
    }

    "be deletable by id" in new WithApplication {
      TeacherDomainComponent.deleteTeacher(PersonTestData.teacher)
    }
  }

}
