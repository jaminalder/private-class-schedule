package dataaccess.lesson

import dataaccess.base.DataAccessObject
import crosscutting.basetype.Id
import com.mongodb.casbah.Imports._
import crosscutting.transferobject.lesson.Lesson

trait LessonDataAccessObject extends DataAccessObject[Lesson]  {

  override val collectionName = "lesson"

  override def converter = LessonDBObjectConverter

  def deleteByID(id:Id) = collection.remove(MongoDBObject("_id" -> id._id))

  def getLessonsOfTeacher(teacherId: Id):List[Lesson] = {
    val lessonIterator = collection.find(MongoDBObject("teacherId" -> teacherId._id))
    lessonIterator.map(converter.dBObjectToTransferObject).toList
  }

}

