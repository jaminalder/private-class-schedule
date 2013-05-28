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
      TeacherDomainComponent.deleteTeacherById(PersonTestData.teacher.id)
    }
/*
    "Store some Students for a Teacher" in new WithApplication {

      for (i <- 1 to 20) studentDAO.persist(PersonTestData.randomStudentOfTeacher)
    }

    "Find all Students of a Teacher" in new WithApplication {
      val storedTeacher = teacherDAO.getById(PersonTestData.teacher.person.id).get
      val studentsOfTeacher = studentDAO.getStudentsOfTeacher(storedTeacher.id)
      studentsOfTeacher.size mustEqual 20
    }

    "Delete some persons" in new WithApplication {
      val storedTeacher: Teacher = teacherDAO.getById(PersonTestData.teacher.person.id).get
      val studentsOfTeacher: List[Student] = studentDAO.getStudentsOfTeacher(storedTeacher.id)
      studentsOfTeacher.foreach(student => studentDAO.deleteByID(student.id))
      teacherDAO.deleteByID(storedTeacher.id)
      teacherDAO.getById(PersonTestData.teacher.person.id) must beNone
      studentDAO.getStudentsOfTeacher(storedTeacher.id).size mustEqual 0
    }
*/
  }


}
