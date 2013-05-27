package conversion.json

import org.specs2.mutable._
import play.api.libs.json.Json
import crosscutting.basetype.Id
import crosscutting.transferobject.base.ImplicitJsonFormats._
import crosscutting.transferobject.person.{Address, Person}

class PersonJsonConverterTest extends Specification {

  val person = Person(id=Id.generate, lastName="Meier", firstName="Hans", eMail="hans.meier@gmail.com",
    address=Address(street="street", streetNum = "3", city = "Bern", zip = "8000"), ownerID=Id.rootID)

  val jsonStringPerson = """{"id":{"_id":"""" + person.id._id + """"},"lastName":"Meier","firstName":"Hans","eMail":"hans.meier@gmail.com","address":{"street":"street","streetNum":"3","city":"Bern","zip":"8000"}, "ownerID":{"_id":"42"}}"""

  "A Person" should {

    "be convertible to json" in {
      (Json.toJson(person) \ "lastName").as[String] mustEqual "Meier"
    }

    "be constructable from json" in {
      Json.parse(jsonStringPerson).as[Person] mustEqual person
    }
  }

}
