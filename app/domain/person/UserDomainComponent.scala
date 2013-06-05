package domain.person

import crosscutting.transferobject.person.Teacher
import dataaccess.person.{RoleDataAccessObject, TeacherDataAccessObject}
import crosscutting.basetype.Id
import com.mongodb.WriteResult

trait UserDomainComponent {

  val dao: TeacherDataAccessObject

  def registerUserAsTeacher(teacher: Teacher, password: String) = dao.registerUserAsTeacher(teacher, password)

  def authenticateUserAsTeacher(eMail: String, password: String): Option[Teacher] = {
    dao.getPasswordByEmail(eMail) match {
      case Some(pwd) if pwd.equals(password) => dao.getByEMail(eMail)
      case _ => None
    }
  }

}

object UserDomainComponent extends UserDomainComponent {
  val dao = TeacherDataAccessObject
}
