package dataaccess

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClientURI
import com.mongodb.util.JSON
import org.specs2.mutable._
import play.api._
import libs.json._
import test.WithApplication
import com.github.nscala_time.time.Imports._
import org.joda.time

class MongoDBTest extends Specification {

  "MongoDB" should {
    "store and fetch data" in new WithApplication {
      val mongoURI: MongoClientURI = MongoClientURI(Play.configuration.getString("mongo.uri").getOrElse("mongodb://127.0.0.1:27017"))
      val mongoDB: MongoDB = MongoClient(mongoURI)("pcs")
      val mongoColl = mongoDB("testcoll")

      mongoColl.drop()

      val newObj: DBObject = MongoDBObject("foo" -> "bar", "x" -> "y", "pie" -> 3.14, "spam" -> "eggs")
      println("mongo object before save: " + newObj)
      mongoColl += newObj
      val storedObject: mongoColl.T = mongoColl.findOne().get
      storedObject.as[String]("foo") mustEqual "bar"

    }

    "store and fetch date times" in new WithApplication {
      val mongoURI: MongoClientURI = MongoClientURI(Play.configuration.getString("mongo.uri").getOrElse("mongodb://127.0.0.1:27017"))
      val mongoDB: MongoDB = MongoClient(mongoURI)("pcs")
      val mongoColl = mongoDB("testcoll")

      mongoColl.drop()

      val dateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")
      val startTime: DateTime = dateFormatter.parseDateTime("07.08.2005 15:55")
      val endTime: DateTime = dateFormatter.parseDateTime("07.08.2005 16:25")

      val newObj = MongoDBObject("startTime" -> startTime, "endTime" -> endTime)
      println("mongo object before save: " + newObj)
      mongoColl += newObj
      val storedObject: mongoColl.T = mongoColl.findOne().get
      val storedStart: DateTime = storedObject.as[DateTime]("startTime")
      val storedEnd: DateTime = storedObject.as[DateTime]("endTime")

      storedStart mustEqual startTime
      storedEnd mustEqual endTime

      (storedStart to storedEnd).toDuration.getStandardMinutes mustEqual 30

    }
  }

  case class DummyObject(foo:String, bar:String)
  implicit val dummyObjectFormat = Json.format[DummyObject]

  "A dummy case class object" should {
    "be convertable from and to json and MongoDBObject" in new WithApplication {

      val mongoURI: MongoClientURI = MongoClientURI(Play.configuration.getString("mongo.uri").getOrElse("mongodb://127.0.0.1:27017"))
      val mongoDB: MongoDB = MongoClient(mongoURI)("pcs")
      val mongoColl = mongoDB("testcoll")

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

    "be converted implicitly and stored into mongodb" in new WithApplication {

      val mongoURI: MongoClientURI = MongoClientURI(Play.configuration.getString("mongo.uri").getOrElse("mongodb://127.0.0.1:27017"))
      val mongoDB: MongoDB = MongoClient(mongoURI)("pcs")
      val mongoColl = mongoDB("testcoll")

      mongoColl.drop()
      mongoColl += DummyObject("ffff", "bbbbb")
      val storedObject: mongoColl.T = mongoColl.findOne().get
      storedObject.as[String]("foo") mustEqual "ffff"
      storedObject._id must beSome[ObjectId]
    }


  }

}