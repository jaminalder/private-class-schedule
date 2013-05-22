package dataaccess.person

import com.mongodb.casbah.Imports._
import domain.person.{Address, Person}
import domain.role.Role
import crosscutting.basetype.Id

object RoleMongoConverter {

  implicit def roleToDBObject(role: Role): DBObject = {

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
      "role" -> role.name,
      "ownerID" -> person.ownerID._id
    )

    personDataObject
  }

  def toRole(dataObject:DBObject): Role = {

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

    val roleName = dataObject.as[String]("role")

    Role.byName(person, roleName)
  }

}
