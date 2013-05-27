package crosscutting.transferobject.person

import crosscutting.basetype.Id

case class Person(id: Id,
                  lastName: String,
                  firstName: String,
                  eMail: String,
                  address: Address,
                  ownerID: Id)

case class Address(street:String, streetNum:String, city:String, zip:String)

