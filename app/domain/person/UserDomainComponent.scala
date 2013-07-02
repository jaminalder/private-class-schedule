package domain.person

import crosscutting.transferobject.person.Teacher
import dataaccess.person.{RoleDataAccessObject, TeacherDataAccessObject}
import crosscutting.basetype.Id
import com.mongodb.WriteResult

trait UserDomainComponent {

  val dao: TeacherDataAccessObject

  def registerUserAsTeacher(teacher: Teacher, password: String): Option[Teacher] = {
    dao.getPasswordByEmail(teacher.person.eMail) match {
      case Some(_) => None // there is already a user with this eMail
      case None => {
        dao.registerUserAsTeacher(teacher, password)
        authenticateUserAsTeacher(teacher.person.eMail, password)
      }
    }
  }

  def authenticateUserAsTeacher(eMail: String, password: String): Option[Teacher] = {
    dao.getPasswordByEmail(eMail) match {
      case Some(pwd) if pwd.equals(password) => dao.getByEMail(eMail)
      case _ => None
    }
  }

}

