package service.user

import play.api.mvc._
import play.api.libs.json.Json
import play.api.Logger
import crosscutting.basetype.Id
import domain.person.TeacherDomainComponent
import crosscutting.transferobject.person.{Person, Teacher}
import crosscutting.transferobject.base.ImplicitJsonFormats._

/**
 * JSON Service.
 * Responsible for User handling and authentication.
 * @todo implement real user authentication here later
 *       this is just a dummy implementation of a login so far
 */
object UserService extends Controller {

  /**
   * Logs in a user with eMail and password.
   * Yields the logged in user as a person json object if the login was successful.
   * Throws an exception otherwise.
   * @todo implement a real login service
   * @param eMail
   * @param password
   * @return the successfully logged in user as a person json object
   */
  def login(eMail: String, password: String) = Action {
    Logger.info("### login action with " + eMail)
    Logger.info("persons in db:")
    TeacherDomainComponent.dao.collection.find.foreach(dbobject => Logger.info(dbobject.toString))
    val storedRole: Teacher = TeacherDomainComponent.getTeacherByEmail(eMail).get
    val jsonPerson = Json.toJson(storedRole.person)
    Ok(jsonPerson).withSession("loggedInUserID" -> storedRole.person.id._id)
  }

  /**
   * Registers a new user in the role of a teacher.
   * @todo implement real registration with passwords etc.
   * @return the new teacher user which is directly logged in.
   */
  def registerUserAsTeacher =  Action(parse.json) {
    request =>
      val newUser: Person = request.body.as[Person]
      val newTeacherUser = Teacher(newUser)
      TeacherDomainComponent.saveTeacher(newTeacherUser)
      Ok(Json.toJson(newUser))
  }

  def deleteUser(id:String) = Action {
    TeacherDomainComponent.deleteTeacherById(Id(id));
    Ok
  }

}
