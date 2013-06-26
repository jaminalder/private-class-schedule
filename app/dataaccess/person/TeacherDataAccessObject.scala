package dataaccess.person

import com.mongodb.casbah.Imports._
import crosscutting.transferobject.person.Teacher

trait TeacherDataAccessObject extends RoleDataAccessObject[Teacher] {
  def converter = TeacherDBObjectConverter

  def registerUserAsTeacher(teacher:Teacher, password:String): Unit = {
    val dbObject = converter.transferObjectToDBObject(teacher)
    val dbObjectWithPassword = dbObject += ("password" -> password)
    persist(teacher.id._id, dbObjectWithPassword)
  }

  def getPasswordByEmail(eMail:String):Option[String] =
    collection.findOne(MongoDBObject("eMail" -> eMail)).map(dbObject => dbObject.as[String]("password"))


}

