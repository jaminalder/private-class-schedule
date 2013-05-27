package dataaccess.person

import com.mongodb.casbah.Imports._
import crosscutting.transferobject.person.Student
import crosscutting.basetype.Id

trait StudentDataAccessObject extends RoleDataAccessObject[Student]{
  def converter = StudentDBObjectConverter

  def getStudentsOfTeacher(teacherId: Id):List[Student] = {
    val studentIterator = collection.find(MongoDBObject("ownerID" -> teacherId._id, "role" -> "Student"))
    studentIterator.map(converter.dBObjectToTransferObject).toList
  }

}

object StudentDataAccessObject extends StudentDataAccessObject
