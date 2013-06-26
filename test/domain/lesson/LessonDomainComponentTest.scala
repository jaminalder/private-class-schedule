package domain.lesson

import org.specs2.mutable._
import play.api.test.WithApplication
import crosscutting.basetype.Id
import org.joda.time.DateTime
import crosscutting.datetime.DateTimeFormats._
import scala.Some
import play.api.libs.json.Json
import crosscutting.transferobject.lesson.Lesson
import domain.person.PersonTestData
import service.WiringModule.LessonDomainComponent

object LessonTestData {
  val teacherId = PersonTestData.teacher.person.id
  val studentIds = List(Id.generate, Id.generate)

  val lesson1Id = Id.generate
  val lesson1Start = DateTime.parse("03.05.2013 1330", simpleDateTimeFormat)
  val lesson1End = DateTime.parse("03.05.2013 1430", simpleDateTimeFormat)
  val lesson1 = Lesson(lesson1Id, lesson1Start, lesson1End, teacherId,  studentIds)

  val lesson2Id = Id.generate
  val lesson2Start = DateTime.parse("20.06.2013 0900", simpleDateTimeFormat)
  val lesson2End = DateTime.parse("20.06.2013 0930", simpleDateTimeFormat)
  val lesson2 = Lesson(lesson2Id, lesson2Start, lesson2End,  teacherId,  studentIds)
}

class LessonDomainComponentTest extends Specification {

  import LessonTestData._

  "The LessonDomainComponent" should {

    "store and retrieve lessons" in new WithApplication {
      LessonDomainComponent.saveLesson(lesson1)
      val storedLesson = LessonDomainComponent.getLessonById(lesson1Id)
      storedLesson mustEqual Some(lesson1)
    }
  }

  "A Lesson" should {

    "be converted to and from json" in new WithApplication {

      import crosscutting.transferobject.base.ImplicitJsonFormats.lessonFormat

      val jsonLesson = Json.toJson(lesson1)
      println("jsonLesson: " + Json.stringify(jsonLesson))
      val convertedLesson = jsonLesson.as[Lesson]
      convertedLesson mustEqual lesson1

    }
  }

}
