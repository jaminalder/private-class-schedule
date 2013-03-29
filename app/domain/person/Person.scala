package domain.person

import domain.base.ID

case class Person(_id: String = ID.generate, lastName: String, firstName: String, eMail: String, address: Address) extends ID
