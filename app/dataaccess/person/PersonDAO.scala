package dataaccess.person

import dataaccess.base.DataAccessObject
import com.mongodb.casbah.Imports._
import domain.role.{Student, Teacher, Role}
import RoleMongoConverter._

object PersonDAO extends DataAccessObject {
  val collectionName: String = "person"

  def persist(role:Role) {
    collection.update(MongoDBObject("_id" -> role.person._id), role, upsert = true)
  }

  def getByID(id: String): Role = {
    toRole(collection.findOneByID(id).get)
  }

  def getStudentsOfTeacher(teacher: Teacher):List[Student] = {
    val studentIterator = collection.find(MongoDBObject("ownerID" -> teacher.person._id, "role" -> "Student"))
    studentIterator.map(toRole).toList.asInstanceOf[List[Student]]
  }
}
