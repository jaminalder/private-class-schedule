package dataaccess.person

import play.api.libs.json.Json
import com.mongodb.util.JSON
import com.mongodb.casbah.Imports._
import domain.person.Person
import conversion.json.PersonJsonConverter._
import domain.role.{Teacher, Role}

object RoleMongoConverter {

  implicit def roleToDBObject(role: Role): DBObject = {
    val dbObject: DBObject = JSON.parse(role.person.toJson.toString()).asInstanceOf[DBObject]
    dbObject += "role" -> role.name
  }

  def toRole(dbObject:DBObject): Role = {
    val role = dbObject.getAs[String]("role").get
    val personDbObject = dbObject -= "role"
    val person = Json.parse(personDbObject.toString).as[Person]
    Role.byName(person, role)
  }

}
