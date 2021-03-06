package service.authentication

import play.api.mvc._
import crosscutting.transferobject.person.{Person, Teacher}
import crosscutting.transferobject.base.ImplicitJsonFormats._
import play.api.libs.json.Json
import crosscutting.basetype.Id
import service.WiringModule.{TeacherDomainComponent, UserDomainComponent}

object AuthenticationService extends Controller with Secured{

  def registerUserAsTeacher = Action(parse.json) {
    request =>
      val user = (request.body \ "user").as[Person]
      val teacher = Teacher(user)
      val password = (request.body \ "password").as[String]
      UserDomainComponent.registerUserAsTeacher(teacher, password) match {
        case Some(teacher: Teacher) => Ok(Json.toJson(teacher.person)).withSession("userId" -> teacher.id._id)
        case _ => Unauthorized("Registration failed").withNewSession
      }
  }

  def loginUserAsTeacher = Action(parse.json) {
    request =>
      val eMail = (request.body \ "eMail").as[String]
      val password = (request.body \ "password").as[String]
      UserDomainComponent.authenticateUserAsTeacher(eMail, password) match {
        case Some(teacher: Teacher) => Ok(Json.toJson(teacher.person)).withSession("userId" -> teacher.id._id)
        case _ => Unauthorized("Authentication failed").withNewSession
      }
  }

  def logoutUser = Action {
    Ok.withNewSession
  }

  def getLoggedInUserAsTeacher = IsAuthenticated {
    userId => request =>
      TeacherDomainComponent.getTeacherById(Id(userId)) match {
        case Some(teacher) => Ok(Json.toJson(teacher.person))
        case _ => NotFound
      }
  }

}
