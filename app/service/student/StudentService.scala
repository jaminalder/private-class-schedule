package service.student

import play.api.mvc._
import play.api.libs.json.{Json, JsValue}
import crosscutting.basetype.Id
import crosscutting.transferobject.person.{Teacher, Student, Person}
import crosscutting.transferobject.base.ImplicitJsonFormats._
import service.WiringModule.{TeacherDomainComponent, StudentDomainComponent}
import service.authentication.Secured

/**
 * JSON Service.
 * Responsible for Student handling.
 */
object StudentService extends Controller with Secured {

  /**
   * Adds a new or updates an existing student.
   * Input: one person in json format
   */
  def saveStudent = IsAuthenticated(parse.json) {
    userId => request =>
      val newStudent: Person = request.body.as[Person]
      if (newStudent.ownerID._id.equals(userId)) {
        StudentDomainComponent.saveStudent(Student(newStudent))
        Ok
      } else {
        Forbidden
      }
  }

  /**
   * Deletes a student from the database.
   * Input: one person in json format
   */
  def deleteStudent = IsAuthenticated(parse.json) {
    userId => request =>
      val studentToDelete: Person = request.body.as[Person]
      if (studentToDelete.ownerID._id.equals(userId)) {
        StudentDomainComponent.deleteStudent(Student(studentToDelete))
        Ok
      } else {
        Forbidden
      }
  }

  /**
   * Yields all students where of a teacher.
   * Where teacherID is owner of student.
   * @param teacherID the UUID of the teacher
   * @return a list of person objects in json format
   */
  def allStudentsOfTeacher(teacherID: String) = IsAuthenticated {
    userId => request =>
      if (userId.equals(teacherID)) {
        val storedTeacher: Teacher = TeacherDomainComponent.getTeacherById(Id(teacherID)).get
        val students: List[Student] = StudentDomainComponent.getStudentsOfTeacher(storedTeacher)
        println("students of teacher: " + students)
        val jsonStudents: List[JsValue] = students.map(t => Json.toJson(t.person))
        Ok(Json.toJson(jsonStudents))
      } else {
        Forbidden
      }
  }

}
