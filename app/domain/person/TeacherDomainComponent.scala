package domain.person

import crosscutting.transferobject.person.Teacher
import dataaccess.person.{RoleDataAccessObject, TeacherDataAccessObject}
import crosscutting.basetype.Id

trait TeacherDomainComponent {

  val dao: RoleDataAccessObject[Teacher]

  def getTeacherById(id: Id) = dao.getById(id)

  def saveTeacher(teacher: Teacher) = dao.persist(teacher)

  def getTeacherByEmail(eMail: String) = dao.getByEMail(eMail)

  def deleteTeacherById(id: Id) = dao.deleteByID(id)

}

object TeacherDomainComponent extends TeacherDomainComponent {
  val dao = TeacherDataAccessObject
}
