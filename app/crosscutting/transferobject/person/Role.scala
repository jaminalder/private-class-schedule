package crosscutting.transferobject.person

import crosscutting.transferobject.base.PersistableTransferObject

sealed abstract class Role(val person: Person) extends PersistableTransferObject(person.id)

case class Teacher(override val person: Person) extends Role(person)

case class Student(override val person: Person) extends Role(person)


