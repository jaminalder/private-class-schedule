package dataaccess.base

import com.mongodb.casbah.MongoClientURI
import play.api.Play
import play.api.Play.current
import com.mongodb.casbah.Imports._

abstract class DataAccessObject {
  val mongoURI: MongoClientURI = MongoClientURI(Play.configuration.getString("mongo.uri").getOrElse("mongodb://127.0.0.1:27017"))
  val mongoDB: MongoDB = MongoClient(mongoURI)("pcs")
  def collection: MongoCollection = mongoDB(collectionName)
  def collectionName:String
}
