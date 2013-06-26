package domain.lesson

import dataaccess.base.DataAccessObject
import crosscutting.transferobject.lesson.Lesson
import crosscutting.basetype.Id
import dataaccess.lesson.LessonDataAccessObject
import crosscutting.transferobject.person.Teacher


trait LessonDomainComponent {

  val dao: LessonDataAccessObject

  def getLessonById(id: Id): Option[Lesson] = dao.getById(id)

  def saveLesson(lesson: Lesson) = dao.persist(lesson)

  def deleteLesson(lesson: Lesson) = dao.deleteByID(lesson.id)

  def getLessonsOfTeacher(teacher: Teacher) = dao.getLessonsOfTeacher(teacher.id)

/*  def deleteLessonsOfTeacher(teacher: Teacher) = {
    val lessonsOfTeacher: List[Lesson] = getLessonsOfTeacher(teacher)
    lessonsOfTeacher.foreach(lesson => dao.deleteByID(lesson.id))
  }   */


}

