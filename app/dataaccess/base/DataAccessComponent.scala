package dataaccess.base

import domain.base.StorableDomainComponent
import com.mongodb.casbah.MongoClientURI
import play.api.Play
import play.api.Play.current
import com.mongodb.casbah.Imports._
import crosscutting.basetype.Id

trait DataAccessComponent {
  self:StorableDomainComponent with DataConverterComponent =>

  val dao: DataAccessObject[StorableDomainObjectType]

  trait DataAccessObject[DO <: StorableDomainObjectType] {
    val mongoURI: MongoClientURI = MongoClientURI(Play.configuration.getString("mongo.uri").getOrElse("mongodb://127.0.0.1:27017"))
    val mongoDB: MongoDB = MongoClient(mongoURI)("pcs")
    def collection: MongoCollection = mongoDB(collectionName)

    def collectionName:String

    def persist(domainObject:DO)
    def getById(id:Id):Option[DO]

  }

}
