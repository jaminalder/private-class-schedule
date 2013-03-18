package poc

import java.util.UUID
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClientURI
import play.api.Play.current
import play.api.Play

object ToDoMongoAccess {
  val mongoURI = MongoClientURI(Play.configuration.getString("mongo.uri").getOrElse("mongodb://127.0.0.1:27017"))
  val mongoColl = MongoClient(mongoURI)("pcs")("todo")

  def convertToMongo(todo:ToDo):MongoDBObject = {
    MongoDBObject("_id" -> todo.id, "text" -> todo.text, "done" -> todo.done)
  }

  def convertFromMongo(mongoObject:MongoDBObject):ToDo = {
    ToDo(Some(mongoObject.as[String]("_id")), mongoObject.as[String]("text"), mongoObject.as[Boolean]("done"))
  }

  def persist(todo: ToDo): ToDo = {
    todo.id match {
      case None => {
        val todoWithID = todo.copy(id = Some(UUID.randomUUID().toString()))
        mongoColl += convertToMongo(todoWithID)
        return todoWithID
      }
      case Some(id) => {
        mongoColl.update(MongoDBObject("_id" -> id), convertToMongo(todo))
        return todo
      }
    }
  }

  def delete(id: String) = {
    mongoColl.remove(MongoDBObject("_id" -> id))
  }

  def getAll: List[ToDo] = {
    val v: Iterator[ToDo] = for(m <- mongoColl.find()) yield convertFromMongo(m)
    v.toList
  }

}
