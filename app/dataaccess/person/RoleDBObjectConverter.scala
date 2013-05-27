package dataaccess.person

import dataaccess.base.DBObjectConverter
import crosscutting.transferobject.person._
import com.mongodb.casbah.Imports._
import crosscutting.basetype.Id

sealed abstract class RoleDBObjectConverter[ConcreteRole <: Role] extends DBObjectConverter[ConcreteRole] {

  def roleName: String

  def createConcreteRole(person: Person): ConcreteRole

  override def transferObjectToDBObject(role: ConcreteRole): DBObject = {

    val address = role.person.address
    val addressDataObject = MongoDBObject(
      "street" -> address.street,
      "streetNum" -> address.streetNum,
      "city" -> address.city,
      "zip" -> address.zip
    )

    val person = role.person
    val personDataObject = MongoDBObject(
      "_id" -> person.id._id,
      "lastName" -> person.lastName,
      "firstName" -> person.firstName,
      "eMail" -> person.eMail,
      "address" -> addressDataObject,
      "role" -> roleName,
      "ownerID" -> person.ownerID._id
    )

    personDataObject
  }

  override def dBObjectToTransferObject(dataObject: DBObject): ConcreteRole = {

    val storedRoleName = dataObject.as[String]("role")
    if (!roleName.equals(storedRoleName)) {
      throw new IllegalArgumentException("trying to make a: " + roleName + " from a " + storedRoleName)
    }

    val addressDBObject = dataObject.as[DBObject]("address")
    val address = Address(
      addressDBObject.as[String]("street"),
      addressDBObject.as[String]("streetNum"),
      addressDBObject.as[String]("city"),
      addressDBObject.as[String]("zip")
    )

    val person = Person(
      Id(dataObject.as[String]("_id")),
      dataObject.as[String]("lastName"),
      dataObject.as[String]("firstName"),
      dataObject.as[String]("eMail"),
      address,
      Id(dataObject.as[String]("ownerID"))
    )

    createConcreteRole(person)

  }

}

object TeacherDBObjectConverter extends RoleDBObjectConverter[Teacher] {
  def roleName: String = "Teacher"

  def createConcreteRole(person: Person): Teacher = Teacher(person)
}

object StudentDBObjectConverter extends RoleDBObjectConverter[Student] {
  def roleName: String = "Student"

  def createConcreteRole(person: Person): Student = Student(person)
}

