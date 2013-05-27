package dataaccess.base

import com.mongodb.casbah.Imports._
import crosscutting.transferobject.base.PersistableTransferObject

trait DBObjectConverter[TO <: PersistableTransferObject] {
  def dBObjectToTransferObject(dataObject: DBObject): TO

  def transferObjectToDBObject(domainObject: TO): DBObject
}
