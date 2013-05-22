package domain.base

import crosscutting.basetype.Id
import dataaccess.base.DataAccessComponent

trait StorableDomainComponent extends DomainComponent{
  self:DataAccessComponent =>

  type StorableDomainObjectType <: StorableDomainObject

  trait StorableDomainObject extends DomainObject{
    self:StorableDomainObjectType =>

    def id:Id
    def save = dao.persist(this)
  }

  trait StorableDomainCompanionObject extends DomainCompanionObject{
    def getById(id:Id):Option[StorableDomainObjectType] = dao.getById(id)
  }

}
