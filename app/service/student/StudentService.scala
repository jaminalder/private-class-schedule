package service.student

import play.api.mvc._
import play.api.libs.json.{Json, JsValue}
import domain.role.{Teacher, Student}
import dataaccess.person.PersonDAO
import conversion.json.PersonJsonConverter._
import domain.person.Person
import poc.ToDoMongoAccess

/**
 * JSON Service.
 * Responsible for Student handling.
 */
object StudentService extends Controller {

  /**
   * Adds a new student.
   * Input: one person in json format
   */
  def add = Action(parse.json) {
    request =>
      println("add student input: " + request.body)
      val newStudent: Person = request.body.as[Person]
      PersonDAO.persist(Student(newStudent))
      Ok
  }

  /**
   * Yields all students where of a teacher.
   * Where teacherID is owner of student.
   * @param teacherID the UUID of the teacher
   * @return a list of person objects in json format
   */
  def allForTeacher(teacherID: String) = Action {
    val storedTeacher = PersonDAO.getByID(teacherID).get.asInstanceOf[Teacher]

    val students: List[Student] = PersonDAO.getStudentsOfTeacher(storedTeacher)
    println("students of teacher: " + students)
    val jsonStudents: List[JsValue] = students.map(t => Json.toJson(t.person))
    Ok(Json.toJson(jsonStudents))
  }

}
