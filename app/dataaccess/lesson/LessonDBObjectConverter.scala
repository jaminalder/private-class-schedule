package dataaccess.lesson

import dataaccess.base.DBObjectConverter
import crosscutting.transferobject.lesson.Lesson
import com.mongodb.casbah.Imports._
import crosscutting.basetype.Id
import org.joda.time.DateTime


object LessonDBObjectConverter extends DBObjectConverter[Lesson] {
  override def dBObjectToTransferObject(dataObject: DBObject): Lesson =
    Lesson(
      Id(dataObject.as[String]("_id")),
      dataObject.as[DateTime]("start"),
      dataObject.as[DateTime]("end"),
      Id(dataObject.as[String]("teacherId")),
      dataObject.as[MongoDBList]("studentIds").toList.asInstanceOf[List[String]].map(stringValue => Id(stringValue))
    )

  override def transferObjectToDBObject(domainObject: Lesson): DBObject =
    MongoDBObject(
      "_id" -> domainObject.id._id,
      "start" -> domainObject.start,
      "end" -> domainObject.end,
      "teacherId" -> domainObject.teacherId._id,
      "studentIds" -> domainObject.studentIds.map(_._id)
    )

}
