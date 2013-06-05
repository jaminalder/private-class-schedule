package domain.lesson

import org.specs2.mutable._
import play.api.test.WithApplication
import crosscutting.basetype.Id
import org.joda.time.DateTime
import crosscutting.datetime.DateTimeFormats._
import scala.Some
import play.api.libs.json.Json
import crosscutting.transferobject.lesson.Lesson

object LessonTestData {
  val lessonId = Id.generate
  val start = DateTime.parse("03.05.2013 1330", simpleDateTimeFormat)
  val end = DateTime.parse("03.05.2013 1430", simpleDateTimeFormat)
  val teacherId = Id.generate
//  val studentIds = List(Id.generate, Id.generate)
  val lesson = Lesson(lessonId, start, end, teacherId /* ,  studentIds */ )
}

class LessonDomainComponentTest extends Specification {

  import LessonTestData._

  "The LessonDomainComponent" should {

    "store and retrieve lessons" in new WithApplication {
      LessonDomainComponent.saveLesson(lesson)
      val storedLesson = LessonDomainComponent.getLessonById(lessonId)
      storedLesson mustEqual Some(lesson)
    }
  }

  "A Lesson" should {

    "be converted to and from json" in new WithApplication {

      import crosscutting.transferobject.base.ImplicitJsonFormats.lessonFormat

      val jsonLesson = Json.toJson(lesson)
      println("jsonLesson: " + Json.stringify(jsonLesson))
      val convertedLesson = jsonLesson.as[Lesson]
      convertedLesson mustEqual lesson

    }
  }

}
