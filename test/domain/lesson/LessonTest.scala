package domain.lesson

import org.specs2.mutable._
import play.api.test.WithApplication
import crosscutting.basetype.Id
import org.joda.time.DateTime
import crosscutting.datetime.DateTimeFormats._
import scala.Some
import dataaccess.lesson.{LessonDataConverterComponent, LessonDataAccessComponent}
import crosscutting.json.JsonConverterComponent
import play.api.libs.json.{Format, Json}

class LessonTest extends Specification {

  "A Lesson" should {

    "be storable and retrievable" in new WithApplication {

      import WiringComponent.Lesson

      val lessonId = Id.generate
      val start = DateTime.parse("03.05.2013 1330", simpleDateTimeFormat)
      val end = DateTime.parse("03.05.2013 1430", simpleDateTimeFormat)
      val teacherId = Id.generate
      val studentIds = List(Id.generate, Id.generate)

      val lesson = Lesson(lessonId, start, end, teacherId, studentIds)

      lesson.save

      val storedLesson = Lesson.getById(lessonId)

      storedLesson mustEqual Some(lesson)

    }
    "be converted to and from json" in new WithApplication {
      import WiringComponent.{Lesson, jsonConverter}

      val lessonId = Id.generate
      val start = DateTime.parse("03.05.2013 1330", simpleDateTimeFormat)
      val end = DateTime.parse("03.05.2013 1430", simpleDateTimeFormat)
      val teacherId = Id.generate
      val studentIds = List(Id.generate, Id.generate)

      val lesson = Lesson(lessonId, start, end, teacherId, studentIds)

      val jsonLesson = jsonConverter.domainToJson(lesson)
      println("jsonLesson: " + Json.stringify(jsonLesson))

      val convertedLesson = jsonConverter.jsonToDomain(jsonLesson)

      convertedLesson mustEqual lesson

    }
  }

  object WiringComponent
    extends LessonDomainComponent
    with LessonDataAccessComponent
    with LessonDataConverterComponent
    with JsonConverterComponent {

    implicit val format: Format[Lesson] = Json.format[Lesson]

    val dao = LessonDataAccessObject
    val dataObjectConverter = LessonDataObjectConverter
  }

}
