package service.student

import play.api.mvc._
import play.api.libs.json.{Json, JsValue}
import crosscutting.basetype.Id
import crosscutting.transferobject.person.{Teacher, Student, Person}
import domain.person.{TeacherDomainComponent, StudentDomainComponent}
import crosscutting.transferobject.base.ImplicitJsonFormats._

/**
 * JSON Service.
 * Responsible for Student handling.
 */
object StudentService extends Controller {

  /**
   * Adds a new or updates an existing student.
   * Input: one person in json format
   */
  def saveStudent = Action(parse.json) {
    request =>
      println("add student input: " + request.body)
      val newStudent: Person = request.body.as[Person]
      StudentDomainComponent.saveStudent(Student(newStudent))
      Ok
  }

  /**
   * Deletes a student from the database.
   * Input: one person in json format
   */
  def deleteStudent = Action(parse.json) {
    request =>
      println("delete student input: " + request.body)
      val studentToDelete: Person = request.body.as[Person]
      StudentDomainComponent.deleteStudentById(studentToDelete.id)
      Ok
  }

  /**
   * Yields all students where of a teacher.
   * Where teacherID is owner of student.
   * @param teacherID the UUID of the teacher
   * @return a list of person objects in json format
   */
  def allStudentsOfTeacher(teacherID: String) = Action {
    val storedTeacher: Teacher = TeacherDomainComponent.getTeacherById(Id(teacherID)).get
    val students: List[Student] = StudentDomainComponent.getStudentsOfTeacher(storedTeacher.id)
    println("students of teacher: " + students)
    val jsonStudents: List[JsValue] = students.map(t => Json.toJson(t.person))
    Ok(Json.toJson(jsonStudents))
  }

}
