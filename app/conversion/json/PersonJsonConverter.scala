package conversion.json

import play.api.libs.json.{JsValue, Json}
import domain.person.{Person, Address}

object PersonJsonConverter {

  implicit val addressFormat = Json.format[Address]
  implicit val personFormat = Json.format[Person]

  class JsonPerson(person:Person){
    def toJson: JsValue = Json.toJson(person)
  }

  implicit def personToJsonPerson(person:Person):JsonPerson = new JsonPerson(person)
  def toPerson(jsValue:JsValue): Person = jsValue.as[Person]
  def toPerson(jsString:String): Person = Json.parse(jsString).as[Person]

}
