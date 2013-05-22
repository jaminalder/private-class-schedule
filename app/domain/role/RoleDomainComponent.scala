package domain.role

import domain.base.StorableDomainComponent
import dataaccess.role.RoleDataAccessComponent
import domain.person.PersonDomainComponent

trait RoleDomainComponent extends StorableDomainComponent {
  self:PersonDomainComponent with RoleDataAccessComponent =>

  override type StorableDomainObjectType = Role

  abstract class Role(val person:Person) extends StorableDomainObject {
    def name: String = this.getClass.getSimpleName
    def id = person.id
  }

  object Role {

    def byName(person:Person, roleName:String):Role = roleName match {
      case "Teacher" => Teacher(person)
      case "Student" => Student(person)
      case _ => throw new IllegalArgumentException("role not existing: " + roleName)
    }

  }

  case class Teacher(override val person:Person) extends Role(person)
  object Teacher extends StorableDomainCompanionObject

  case class Student(override val person:Person) extends Role(person)
  object Student extends StorableDomainCompanionObject

}
