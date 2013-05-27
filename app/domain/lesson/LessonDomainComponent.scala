package domain.lesson

import dataaccess.base.DataAccessObject
import crosscutting.transferobject.lesson.Lesson
import crosscutting.basetype.Id
import dataaccess.lesson.LessonDataAccessObject


trait LessonDomainComponent {

  val dao: DataAccessObject[Lesson]

  def getLessonById(id: Id): Option[Lesson] = dao.getById(id)
  def saveLesson(lesson: Lesson) = dao.persist(lesson)

}

object LessonDomainComponent extends LessonDomainComponent{
  val dao = LessonDataAccessObject
}
