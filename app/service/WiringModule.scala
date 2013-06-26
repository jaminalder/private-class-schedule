package service

import dataaccess.lesson.LessonDataAccessObject
import dataaccess.person.{TeacherDataAccessObject, StudentDataAccessObject}
import domain.lesson.LessonDomainComponent
import domain.person.{UserDomainComponent, TeacherDomainComponent, StudentDomainComponent}

/**
 * This object wires domain and data access traits together
 * and exposes concrete domain objects to be used in the services.
 */
object WiringModule {

  object LessonDomainComponent extends LessonDomainComponent {
    val dao = new LessonDataAccessObject {}
  }

  object StudentDomainComponent extends StudentDomainComponent {
    val dao = new StudentDataAccessObject {}
  }

  object TeacherDomainComponent extends TeacherDomainComponent {
    val dao = new TeacherDataAccessObject {}
  }

  object UserDomainComponent extends UserDomainComponent {
    val dao = new TeacherDataAccessObject {}
  }

}
