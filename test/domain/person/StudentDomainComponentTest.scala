package domain.person

import org.specs2.mutable._
import play.api.test.WithApplication
import service.WiringModule.{StudentDomainComponent, TeacherDomainComponent}

class StudentDomainComponentTest extends Specification {

  sequential

  "The StudentDomainComponent" should {

    "remove all persons and add a teacher first" in new WithApplication {
      TeacherDomainComponent.dao.collection.drop
      TeacherDomainComponent.saveTeacher(PersonTestData.teacher)
      success
    }

    "Store some Students for a Teacher" in new WithApplication {
      for (i <- 1 to 20) StudentDomainComponent.saveStudent(PersonTestData.randomStudentOfTeacher)
      success
    }

    "Find all Students of a Teacher" in new WithApplication {
      val studentsOfTeacher = StudentDomainComponent.getStudentsOfTeacher(PersonTestData.teacher)
      studentsOfTeacher.size mustEqual 20
    }

    "delete all students of a teacher" in new WithApplication {
      StudentDomainComponent.deleteStudentsOfTeacher(PersonTestData.teacher)
      val studentsOfTeacher = StudentDomainComponent.getStudentsOfTeacher(PersonTestData.teacher)
      studentsOfTeacher.size mustEqual 0
    }

    "cleanup the stored teacher" in new WithApplication {
      TeacherDomainComponent.deleteTeacher(PersonTestData.teacher)
      TeacherDomainComponent.getTeacherById(PersonTestData.teacher.id) must beNone
    }
  }


}
