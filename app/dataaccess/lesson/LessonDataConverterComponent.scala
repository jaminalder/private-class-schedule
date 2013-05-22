package dataaccess.lesson

import dataaccess.base.DataConverterComponent
import domain.lesson.LessonDomainComponent
import com.mongodb.casbah.Imports._
import crosscutting.basetype.Id
import org.joda.time.DateTime

trait LessonDataConverterComponent extends DataConverterComponent {
  self:LessonDomainComponent =>

  object LessonDataObjectConverter extends DataObjectConverter[Lesson]{
    override def dataToDomain(dataObject: DBObject): Lesson =
      Lesson(
        Id(dataObject.as[String]("_id")),
        dataObject.as[DateTime]("start"),
        dataObject.as[DateTime]("end"),
        Id(dataObject.as[String]("teacherId")),
        dataObject.as[MongoDBList]("studentIds").toList.asInstanceOf[List[String]].map(stringValue => Id(stringValue))
      )

    override def domainToData(domainObject: Lesson): DBObject =
      MongoDBObject(
        "_id" -> domainObject.id._id,
        "start" -> domainObject.start,
        "end" -> domainObject.end,
        "teacherId" -> domainObject.teacherId._id,
        "studentIds" -> domainObject.studentIds.map(_._id)
      )
  }

}
