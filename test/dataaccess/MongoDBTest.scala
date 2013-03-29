package dataaccess

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClientURI
import com.mongodb.util.JSON
import org.specs2.mutable._
import person.RoleMongoConverter._
import play.api._
import libs.json._
import domain.person.{Address, Person}
import domain.person.Person
import domain.person.Address

class MongoDBTest extends Specification {

  val mongoURI: MongoClientURI = MongoClientURI("mongodb://127.0.0.1:27017")
  val mongoDB: MongoDB = MongoClient(mongoURI)("testdb")
  val mongoColl = mongoDB("testcoll")

  "MongoDB" should {
    "store and fetch data" in {

      mongoColl.drop()

      val newObj = MongoDBObject("foo" -> "bar", "x" -> "y", "pie" -> 3.14, "spam" -> "eggs")
      println("mongo object before save: " + newObj)
      mongoColl += newObj
      val storedObject: mongoColl.T = mongoColl.findOne().get
      storedObject.as[String]("foo") mustEqual "bar"

    }
  }

  case class DummyObject(foo:String, bar:String)
  implicit val dummyObjectFormat = Json.format[DummyObject]

  "A dummy case class object" should {
    "be convertable from and to json and MongoDBObject" in {

      val dummy = DummyObject("fooValue", "barValue")
      val dummyJson: JsValue = Json.toJson(dummy)
      val foo: String = (dummyJson \ "foo").as[String]

      foo mustEqual "fooValue"

      val dummyBack = dummyJson.as[DummyObject]

      dummyBack mustEqual dummy

      val dummyDBObject: DBObject = JSON.parse(Json.toJson(dummy).toString()).asInstanceOf[DBObject]

      dummyDBObject("bar") mustEqual "barValue"

      val dbObjectBack = Json.parse(dummyDBObject.toString).as[DummyObject]

      dbObjectBack mustEqual dummy

    }

    implicit def dummyToDBObject(dummy:DummyObject):DBObject = JSON.parse(Json.toJson(dummy).toString()).asInstanceOf[DBObject]

    "be converted implicitly and stored into mongodb" in {
      mongoColl.drop()
      mongoColl += DummyObject("ffff", "bbbbb")
      val storedObject: mongoColl.T = mongoColl.findOne().get
      storedObject.as[String]("foo") mustEqual "ffff"
      storedObject._id must beSome[ObjectId]
    }


  }

}