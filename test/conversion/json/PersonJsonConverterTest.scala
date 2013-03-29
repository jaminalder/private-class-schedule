package conversion.json

import org.specs2.mutable._
import play.api.libs.json.Json
import conversion.json.PersonJsonConverter._
import domain.role.{Teacher, Role}
import domain.person.{Address, Person}

class PersonJsonConverterTest extends Specification {

  val person = Person(lastName="Meier", firstName="Hans", eMail="hans.meier@gmail.com",
    address=Address(street="street", streetNum = "3", city = "Bern", zip = "8000"))

  val jsonStringPerson = """{"_id":"""" + person._id + """","lastName":"Meier","firstName":"Hans","eMail":"hans.meier@gmail.com","address":{"street":"street","streetNum":"3","city":"Bern","zip":"8000"}}"""

  "A Person" should {

    "be convertible to json" in {
      (person.toJson \ "lastName").as[String] mustEqual "Meier"
    }

    "be constructable from json" in {
      toPerson(Json.parse(jsonStringPerson)) mustEqual person
      toPerson(jsonStringPerson) mustEqual person
    }
  }

}
