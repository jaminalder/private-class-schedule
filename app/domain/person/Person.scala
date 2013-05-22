package domain.person

import crosscutting.basetype.Id

case class Person(id: Id,
                  lastName: String,
                  firstName: String,
                  eMail: String,
                  address: Address,
                  ownerID: Id)
