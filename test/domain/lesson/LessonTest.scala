package domain.lesson

import org.specs2.mutable._
import play.api.test.WithApplication
import crosscutting.basetype.Id
import org.joda.time.DateTime
import crosscutting.datetime.DateTimeFormats._
import scala.Some
import dataaccess.lesson.{LessonDBObjectConverter, LessonDataAccessObject}
import play.api.libs.json.{Format, Json}
import crosscutting.transferobject.lesson.Lesson

class LessonTest extends Specification {

  "A Lesson" should {

    "be storable and retrievable" in new WithApplication {

      val lessonId = Id.generate
      val start = DateTime.parse("03.05.2013 1330", simpleDateTimeFormat)
      val end = DateTime.parse("03.05.2013 1430", simpleDateTimeFormat)
      val teacherId = Id.generate
      val studentIds = List(Id.generate, Id.generate)

      val lesson = Lesson(lessonId, start, end, teacherId, studentIds)

      LessonDomainComponent.saveLesson(lesson)

      val storedLesson = LessonDomainComponent.getLessonById(lessonId)

      storedLesson mustEqual Some(lesson)

    }
    "be converted to and from json" in new WithApplication {

      val lessonId = Id.generate
      val start = DateTime.parse("03.05.2013 1330", simpleDateTimeFormat)
      val end = DateTime.parse("03.05.2013 1430", simpleDateTimeFormat)
      val teacherId = Id.generate
      val studentIds = List(Id.generate, Id.generate)

      val lesson = Lesson(lessonId, start, end, teacherId, studentIds)

      import crosscutting.transferobject.base.ImplicitJsonFormats.lessonFormat

      val jsonLesson = Json.toJson(lesson)
      println("jsonLesson: " + Json.stringify(jsonLesson))

      val convertedLesson = jsonLesson.as[Lesson]

      convertedLesson mustEqual lesson

    }
  }


}
