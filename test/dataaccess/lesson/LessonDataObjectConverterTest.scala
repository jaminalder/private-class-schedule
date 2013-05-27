package dataaccess.lesson

import org.specs2.mutable._
import domain.lesson.LessonDomainComponent
import crosscutting.basetype.Id
import org.joda.time.DateTime
import crosscutting.datetime.DateTimeFormats.simpleDateTimeFormat
import com.mongodb.casbah.Imports._
import play.api.test.WithApplication
import crosscutting.transferobject.lesson.Lesson

class LessonDataObjectConverterTest extends Specification {

  "A Lesson" should {
    "be converted to DBObject and back" in new WithApplication {

      val dataObjectConverter = LessonDBObjectConverter

      val lessonId = Id.generate
      val start = DateTime.parse("03.05.2013 1330", simpleDateTimeFormat)
      val end = DateTime.parse("03.05.2013 1430", simpleDateTimeFormat)
      val teacherId = Id.generate
      val studentIds = List(Id.generate, Id.generate)

      val lesson = Lesson(lessonId, start, end, teacherId, studentIds)

      val dataObject = dataObjectConverter.transferObjectToDBObject(lesson)

      dataObject.getAs[String]("_id") must beSome[String]
      dataObject.getAs[String]("_id").get mustEqual lessonId._id
      dataObject.getAs[DateTime]("end").get mustEqual end

      val convertedLesson = dataObjectConverter.dBObjectToTransferObject(dataObject)

      lesson mustEqual convertedLesson

    }
  }


}
