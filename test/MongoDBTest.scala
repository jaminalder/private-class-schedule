import com.mongodb.casbah.Imports._
import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import models.{ToDoMongoAccess, ToDo, AppDB}
import slick.session.Session
import java.util.UUID

class MongoDBTest extends Specification {

  "MongoDB" should {
    "work" in {
      val mongoColl = MongoClient()("casbah_test")("test_data")

      mongoColl.drop()

      val newObj = MongoDBObject("foo" -> "bar", "x" -> "y", "pie" -> 3.14, "spam" -> "eggs")
      println("mongo object before save: " + newObj)
      mongoColl += newObj

      val id: String = UUID.randomUUID().toString()
      val todo: ToDo = ToDo(Some(id), "blabla", false)
      val todoMongo: MongoDBObject = ToDoMongoAccess.convertToMongo(todo)

      mongoColl += todoMongo

      for(x <- mongoColl.find()) println("stored object " + x)

      val queryObject = MongoDBObject("_id" -> id)
      val queryResult: DBObject = mongoColl.findOne(queryObject).get.asDBObject
      val todoFromDB: ToDo = ToDoMongoAccess.convertFromMongo(queryResult)

      println("todo converted back from db: " + todoFromDB)

      todoFromDB mustEqual todo

    }
  }

}