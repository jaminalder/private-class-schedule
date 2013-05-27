package dataaccess.person

import dataaccess.base.{DBObjectConverter, DataAccessObject}
import crosscutting.transferobject.person.Role
import crosscutting.basetype.Id
import com.mongodb.casbah.Imports._

abstract class RoleDataAccessObject[ConcreteRole <: Role] extends DataAccessObject[ConcreteRole] {

  def collectionName: String = "person"

  def converter: RoleDBObjectConverter[ConcreteRole]

  def deleteByID(id:Id) = collection.remove(MongoDBObject("_id" -> id._id))

  def getByEMail(eMail: String): Option[ConcreteRole] = collection.findOne(MongoDBObject("eMail" -> eMail)).map(converter.dBObjectToTransferObject)

}
