package controllers

import play.api._
import libs.json._
import play.api.mvc._
import models.{ToDoMongoAccess, AppDB, ToDo}
import slick.session.Session

object MongoPOCController extends Controller {

  def todos = Action {
    val allTodos: List[ToDo] = ToDoMongoAccess.getAll
    println("todos in db: " + allTodos)
    val jsonTodos: List[JsValue] = allTodos.map(t => Json.toJson(t))
    Ok(Json.toJson(jsonTodos))
  }

  def addTodo = Action(parse.json) {
    request =>
      println("###### new todo json: " + request.body)
      val newTodo = request.body.as[ToDo]
      println("###### new todo object: " + newTodo)

      val storedTodo = ToDoMongoAccess.persist(newTodo)
      println("###### new todo stored: " + storedTodo)
      Ok(Json.toJson(storedTodo))

  }

  def updateTodo(id: String) = addTodo

  def deleteTodo(id: String) = Action {
    ToDoMongoAccess.delete(id)
    Ok
  }

  implicit val toDoFormat = Json.format[ToDo]

}