package dataaccess.base

import com.mongodb.casbah.MongoClientURI
import play.api.Play
import play.api.Play.current
import com.mongodb.casbah.Imports._
import crosscutting.basetype.Id
import crosscutting.transferobject.base.PersistableTransferObject
import com.mongodb

trait DataAccessObject[TO <: PersistableTransferObject] {
  val mongoURI: MongoClientURI = MongoClientURI(Play.configuration.getString("mongo.uri").getOrElse("mongodb://127.0.0.1:27017"))
  val mongoDB: MongoDB = MongoClient(mongoURI)(Play.configuration.getString("mongo.name").getOrElse("pcs"))

  def collection: MongoCollection = mongoDB(collectionName)

  def collectionName: String

  def converter: DBObjectConverter[TO]

  def persist(to:TO): Unit = persist(to.id._id, converter.transferObjectToDBObject(to))

  def persist(id:String, dbObject:DBObject): Unit = collection.update(MongoDBObject("_id" -> id), dbObject, upsert = true)

  def getById(id:Id) = collection.findOneByID(id._id).map(converter.dBObjectToTransferObject)

}
