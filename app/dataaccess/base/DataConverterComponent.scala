package dataaccess.base

import domain.base.StorableDomainComponent
import com.mongodb.casbah.Imports._

trait DataConverterComponent {
  self:StorableDomainComponent =>

  val dataObjectConverter: DataObjectConverter[StorableDomainObjectType]

  trait DataObjectConverter[DO <: StorableDomainObjectType] {
    def dataToDomain(dataObject: DBObject):StorableDomainObjectType
    def domainToData(domainObject: StorableDomainObjectType): DBObject
  }

}
