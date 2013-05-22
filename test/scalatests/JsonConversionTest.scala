package scalatests

import org.specs2.mutable._
import play.api.libs.json.Json
import crosscutting.basetype.Id

class JsonConversionTest extends Specification {

  case class A(value:String)
  //object A
  implicit val aFormat = Json.format[A]

  "A single value case class" should {
    "convert to json with a standard formatter" in {
      val a = A("bla")
      val jsA = Json.toJson(a)
      jsA mustEqual Json.obj("value" -> "bla")

      val id = Id.generate
      val jsId = Json.toJson(id)
      jsId mustEqual Json.obj("_id" -> id._id)
    }
  }

}
