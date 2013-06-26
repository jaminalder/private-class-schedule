package domain.person

import org.specs2.mutable._
import play.api.test.WithApplication
import crosscutting.transferobject.person.Teacher
import service.WiringModule.TeacherDomainComponent

class TeacherDomainComponentTest extends Specification {

  sequential

  "The TeacherDomainComponent" should {

    "remove all persons first" in new WithApplication {
      TeacherDomainComponent.dao.collection.drop
      success
    }

    "store and retrieve teachers by id" in new WithApplication {
      TeacherDomainComponent.saveTeacher(PersonTestData.teacher)
      val storedTeacher: Option[Teacher] = TeacherDomainComponent.getTeacherById(PersonTestData.teacher.person.id)
      storedTeacher.get mustEqual PersonTestData.teacher
    }

    "retrieve teacher by email" in new WithApplication {
      val storedTeacher: Option[Teacher] = TeacherDomainComponent.getTeacherByEmail("hans.meier@gmail.com")
      storedTeacher.get mustEqual PersonTestData.teacher
    }

    "delete teacher by id" in new WithApplication {
      TeacherDomainComponent.deleteTeacher(PersonTestData.teacher)
    }
  }

}
