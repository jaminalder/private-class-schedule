package domain.role

import domain.person.Person

abstract class Role(val person:Person){
  def name: String = this.getClass.getSimpleName
}
case class Teacher(override val person:Person) extends Role(person)
case class Student(override val person:Person) extends Role(person)

object Role {

  def byName(person:Person, roleName:String):Role = roleName match {
    case "Teacher" => Teacher(person)
    case "Student" => Student(person)
    case _ => throw new IllegalArgumentException("role not existing: " + roleName)
  }

}

