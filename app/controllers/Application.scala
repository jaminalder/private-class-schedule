package controllers

import play.api._
import libs.json._
import libs.functional.syntax._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is not ready."))
  }

  def todos = Action {
    Ok(Json.arr(Json.toJson(ToDo("tatata", false)),Json.toJson(ToDo("trallalla", true))))
  }

  def addTodo = Action(parse.json) { request =>
    println("###### new todo json" + request.body)
    println("###### new todo object" + request.body.as[ToDo])
    Ok
  }

  case class ToDo(text:String,done:Boolean)
  implicit val toDoFormat = Json.format[ToDo]

}