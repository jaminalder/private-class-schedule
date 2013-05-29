package domain.person

import dataaccess.person.StudentDataAccessObject
import crosscutting.basetype.Id
import crosscutting.transferobject.person.{Teacher, Student}

trait StudentDomainComponent {

  val dao: StudentDataAccessObject

  def getStudentById(id: Id) = dao.getById(id)

  def saveStudent(student: Student) = dao.persist(student)

  def deleteStudent(student: Student) = dao.deleteByID(student.id)

  def getStudentsOfTeacher(teacher: Teacher) = dao.getStudentsOfTeacher(teacher.id)

  def deleteStudentsOfTeacher(teacher: Teacher) = {
    val studentsOfTeacher: List[Student] = getStudentsOfTeacher(teacher)
    studentsOfTeacher.foreach(student => dao.deleteByID(student.id))
  }

}

object StudentDomainComponent extends StudentDomainComponent {
  val dao = StudentDataAccessObject
}
