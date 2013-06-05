package service.lesson

import play.api.mvc._
import play.api.libs.json.{Json, JsValue}
import crosscutting.basetype.Id
import crosscutting.transferobject.lesson
import domain.person.{TeacherDomainComponent, StudentDomainComponent}
import domain.lesson.LessonDomainComponent
import crosscutting.transferobject.base.ImplicitJsonFormats._
import crosscutting.transferobject.lesson.Lesson
import crosscutting.transferobject.person.Teacher

/**
 * JSON Service.
 * Responsible for Lesson handling.
 */
object LessonService extends Controller {
  /**
   * Adds a new or updates an existing lesson.
   * Input: one lesson in json format
   */
  def saveLesson = Action(parse.json) {
    request =>
      println("add lesson input: " + request.body)
      val newLesson: Lesson = request.body.as[Lesson]
      LessonDomainComponent.saveLesson(newLesson)
      Ok
  }

  /**
   * Deletes a lesson from the database.
   * Input: one lesson in json format
   */
  def deleteLesson = Action(parse.json) {
    request =>
      println("delete lesson input: " + request.body)
      val lessonToDelete: Lesson = request.body.as[Lesson]
      LessonDomainComponent.deleteLesson(lessonToDelete)
      Ok
  }

  /**
   * Yields all lessons of a teacher.
   * Where teacherID is owner of student.
   * @param teacherID the UUID of the teacher
   * @return a list of person objects in json format
   */
  def allLessonsOfTeacher(teacherID: String) = Action {
    val storedTeacher: Teacher = TeacherDomainComponent.getTeacherById(Id(teacherID)).get
    val lessons: List[Lesson] = LessonDomainComponent.getLessonsOfTeacher(storedTeacher)
    println("lessons of teacher: " + lessons)
    val jsonLessons: List[JsValue] = lessons.map(t => Json.toJson(t))
    Ok(Json.toJson(jsonLessons))
  }

}
