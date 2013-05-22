package domain.lesson

import org.joda.time.DateTime
import domain.base.StorableDomainComponent
import crosscutting.basetype.Id
import dataaccess.lesson.LessonDataAccessComponent
import play.api.libs.json.{Format, Json}

trait LessonDomainComponent extends StorableDomainComponent{
  self:LessonDataAccessComponent =>

  override type StorableDomainObjectType = Lesson

  case class Lesson(id:Id, start:DateTime, end:DateTime, teacherId:Id, studentIds:List[Id]) extends StorableDomainObject

  object Lesson extends StorableDomainCompanionObject with Function5[Id, DateTime, DateTime, Id, List[Id], Lesson] {
    //implicit val lessonFormat = Json.format[Lesson]
  }

}
