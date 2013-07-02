package domain.person

import crosscutting.transferobject.person.{Student, Address, Person, Teacher}
import crosscutting.basetype.Id

object PersonTestData {
  val teacher = Teacher(Person(id = Id.generate, lastName = "Meier", firstName = "Hans", eMail = "hans.meier@gmail.com",
    address = Address(street = "street", streetNum = "3", city = "Bern", zip = "8000"), ownerID = Id.rootID))

  val duplicateTeacher = Teacher(Person(id = Id.generate, lastName = "Meier", firstName = "Hans", eMail = "hans.meier@gmail.com",
    address = Address(street = "street", streetNum = "3", city = "Bern", zip = "8000"), ownerID = Id.rootID))

  def randomStudentOfTeacher: Student = {
    val id = Id.generate
    val namePart = id._id.substring(3, 6)
    Student(Person(id, "last" + namePart, "first" + namePart, namePart + "@server.com",
      Address(namePart + "street", namePart, namePart + "city", namePart), PersonTestData.teacher.person.id))
  }

}
