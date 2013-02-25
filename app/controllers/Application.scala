package controllers

import play.api._
import libs.json._
import libs.functional.syntax._
import play.api.mvc._
import models.{AppDB, ToDo}
import java.util.UUID
import slick.session.Session

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is not ready."))
  }

  def todos = Action {
    AppDB.database.withSession {
      implicit session: Session =>
        val allTodos: List[ToDo] = AppDB.dal.ToDos.getAll
        println("todos in db: " + allTodos)
        val jsonTodos: List[JsValue] = allTodos.map(t => Json.toJson(t))
        Ok(Json.toJson(jsonTodos))
    }
  }

  def addTodo = Action(parse.json) { request =>
    println("###### new todo json: " + request.body)
    val newTodo = request.body.as[ToDo]
    println("###### new todo object: " + newTodo)

    AppDB.database.withSession {
      implicit session: Session =>
        val storedTodo = AppDB.dal.ToDos.persist(newTodo)
        println("###### new todo stored: " + storedTodo)
        Ok(Json.toJson(storedTodo))
    }

  }

  def updateTodo(id:String) = addTodo

  def deleteTodo(id:String) = Action {
    AppDB.database.withSession {
      implicit session: Session =>
        AppDB.dal.ToDos.delete(id)
        Ok
    }
  }

  implicit val toDoFormat = Json.format[ToDo]

}