package dataaccess.person

import dataaccess.base.DataAccessObject
import com.mongodb.casbah.Imports._
import domain.role.{Student, Teacher, Role}
import RoleMongoConverter._
import scala.None

/**
 * Handles the data access of person and role objects.
 */
object PersonDAO extends DataAccessObject {
  val collectionName: String = "person"

  def persist(role:Role) {
    collection.update(MongoDBObject("_id" -> role.person._id), role, upsert = true)
  }

  def delete(role:Role) {
    deleteByID(role.person._id)
  }

  def deleteByID(id:String) {
    collection.remove(MongoDBObject("_id" -> id))
  }

  def getByID(id: String): Option[Role] = {
    collection.findOneByID(id).map(toRole)
  }

  def getByEMail(eMail: String): Option[Role] = {
    collection.findOne(MongoDBObject("eMail" -> eMail)).map(toRole)
  }

  def getStudentsOfTeacher(teacher: Teacher):List[Student] = {
    val studentIterator = collection.find(MongoDBObject("ownerID" -> teacher.person._id, "role" -> "Student"))
    studentIterator.map(toRole).toList.asInstanceOf[List[Student]]
  }
}
