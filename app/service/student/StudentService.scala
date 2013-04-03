package service.student

import play.api.mvc._
import poc.{ToDoMongoAccess, ToDo}
import play.api.libs.json.{Json, JsValue}
import domain.role.{Teacher, Student}
import dataaccess.person.PersonDAO
import conversion.json.PersonJsonConverter._

object StudentService extends Controller {

  def allForTeacher(teacherID: String) = Action {
    val storedTeacher = PersonDAO.getByID(teacherID).asInstanceOf[Teacher]

    val students: List[Student] = PersonDAO.getStudentsOfTeacher(storedTeacher)
    println("students of teacher: " + students)
    val jsonStudents: List[JsValue] = students.map(t => Json.toJson(t.person))
    Ok(Json.toJson(jsonStudents))
  }

}
