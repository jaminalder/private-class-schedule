package dataaccess.lesson

import org.specs2.mutable._
import domain.lesson.LessonDomainComponent
import play.api.test.WithApplication
import crosscutting.basetype.Id
import org.joda.time.DateTime
import crosscutting.datetime.DateTimeFormats._
import crosscutting.transferobject.lesson.Lesson

class LessonDataAccessObjectTest extends Specification {

  "The LessonDataAccessObject" should {
    "store and retrieve a Lesson from the db" in new WithApplication {

      val dao = LessonDataAccessObject

      val lessonId = Id.generate
      val start = DateTime.parse("03.05.2013 1330", simpleDateTimeFormat)
      val end = DateTime.parse("03.05.2013 1430", simpleDateTimeFormat)
      val teacherId = Id.generate
      val studentIds = List(Id.generate, Id.generate)

      val lesson = Lesson(lessonId, start, end, teacherId, studentIds)

      dao.persist(lesson)

      val storedLesson = dao.getById(lessonId)

      storedLesson mustEqual Some(lesson)

    }
  }


}
