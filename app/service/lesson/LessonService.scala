package service.lesson

import play.api.mvc._
import play.api.libs.json.{Json, JsValue}
import crosscutting.basetype.Id
import crosscutting.transferobject.base.ImplicitJsonFormats._
import crosscutting.transferobject.lesson.Lesson
import crosscutting.transferobject.person.Teacher
import service.WiringModule.{TeacherDomainComponent, LessonDomainComponent}
import service.authentication.Secured

/**
 * JSON Service.
 * Responsible for Lesson handling.
 */
object LessonService extends Controller with Secured {
  /**
   * Adds a new or updates an existing lesson.
   * Input: one lesson in json format
   */
  def saveLesson = IsAuthenticated(parse.json) {
    userId => request =>
      val newLesson: Lesson = request.body.as[Lesson]
      if (newLesson.teacherId._id.equals(userId)) {
        LessonDomainComponent.saveLesson(newLesson)
        Ok
      } else {
        Forbidden
      }
  }

  /**
   * Deletes a lesson from the database.
   * Input: one lesson in json format
   */
  def deleteLesson = IsAuthenticated(parse.json) {
    userId => request =>
      val lessonToDelete: Lesson = request.body.as[Lesson]
      if (lessonToDelete.teacherId._id.equals(userId)) {
        LessonDomainComponent.deleteLesson(lessonToDelete)
        Ok
      } else {
        Forbidden
      }
  }

  /**
   * Yields all lessons of a teacher.
   * Where teacherID is owner of student.
   * @param teacherID the UUID of the teacher
   * @return a list of person objects in json format
   */
  def allLessonsOfTeacher(teacherID: String) = IsAuthenticated {
    userId => request =>
      if (teacherID.equals(userId)) {
        val storedTeacher: Teacher = TeacherDomainComponent.getTeacherById(Id(teacherID)).get
        val lessons: List[Lesson] = LessonDomainComponent.getLessonsOfTeacher(storedTeacher)
        println("lessons of teacher: " + lessons)
        val jsonLessons: List[JsValue] = lessons.map(t => Json.toJson(t))
        Ok(Json.toJson(jsonLessons))
      } else {
        Forbidden
      }
  }

}
