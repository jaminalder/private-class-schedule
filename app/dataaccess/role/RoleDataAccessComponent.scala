package dataaccess.role

import dataaccess.base.DataAccessComponent
import domain.role.RoleDomainComponent
import crosscutting.basetype.Id
import com.mongodb.casbah.Imports._

trait RoleDataAccessComponent extends DataAccessComponent {
  self:RoleDomainComponent with RoleDataConverterComponent =>

  object RoleDataAccessObject extends DataAccessObject[Role] {
    def collectionName: String = "person"

    override def persist(domainObject: Role) {
      collection.update(
        MongoDBObject("_id" -> domainObject.person.id._id),
        dataObjectConverter.domainToData(domainObject),
        upsert = true)
    }

    override def getById(id: Id): Option[Role] = collection.findOneByID(id._id).map(dataObjectConverter.dataToDomain)

    def delete(role:Role) {
      deleteByID(role.person.id)
    }

    def deleteByID(id:Id) {
      collection.remove(MongoDBObject("_id" -> id._id))
    }

    def getByEMail(eMail: String): Option[Role] =
      collection.findOne(MongoDBObject("eMail" -> eMail)).map(dataObjectConverter.dataToDomain)


    def getStudentsOfTeacher(teacher: Teacher):List[Student] = {
      val studentIterator = collection.find(MongoDBObject("ownerID" -> teacher.person.id._id, "role" -> "Student"))
      studentIterator.map(dataObjectConverter.dataToDomain).toList.asInstanceOf[List[Student]]
    }

  }

}
