package dataaccess.lesson

import dataaccess.base.DataAccessComponent
import domain.lesson.LessonDomainComponent
import com.mongodb.casbah.Imports._
import crosscutting.basetype.Id

trait LessonDataAccessComponent extends DataAccessComponent {
  self: LessonDomainComponent with LessonDataConverterComponent =>

  object LessonDataAccessObject extends DataAccessObject[Lesson] {

    override val collectionName = "lesson"

    override def persist(lesson:Lesson) = collection.update(
      MongoDBObject("_id" -> lesson.id._id),
      dataObjectConverter.domainToData(lesson),
      upsert = true)

    override def getById(id:Id) = collection.findOneByID(id._id).map(dataObjectConverter.dataToDomain)


  }


}
