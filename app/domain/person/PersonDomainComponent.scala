package domain.person

import domain.base.DomainComponent
import crosscutting.basetype.Id

trait PersonDomainComponent extends DomainComponent {

  case class Person(id: Id,
                    lastName: String,
                    firstName: String,
                    eMail: String,
                    address: Address,
                    ownerID: Id) extends DomainObject

  case class Address(street:String, streetNum:String, city:String, zip:String) extends DomainCompanionObject

}
