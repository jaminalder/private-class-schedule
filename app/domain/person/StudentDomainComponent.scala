package domain.person

import dataaccess.person.StudentDataAccessObject
import crosscutting.basetype.Id
import crosscutting.transferobject.person.Student

trait StudentDomainComponent {

  val dao: StudentDataAccessObject

  def getStudentById(id: Id) = dao.getById(id)

  def saveStudent(student: Student) = dao.persist(student)

  def deleteStudentById(id: Id) = dao.deleteByID(id)

  def getStudentsOfTeacher(teacherId: Id) = dao.getStudentsOfTeacher(teacherId)

  def deleteStudentsOfTeacher(teacherId: Id) = {
    val studentsOfTeacher: List[Student] = getStudentsOfTeacher(teacherId)
    studentsOfTeacher.foreach(student => dao.deleteByID(student.id))
  }

}

object StudentDomainComponent extends StudentDomainComponent {
  val dao = StudentDataAccessObject
}
