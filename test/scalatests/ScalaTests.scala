package scalatests

import org.specs2.mutable._

class ScalaTests extends Specification {

  "Option" should {
    "work as i expect" in {
      val stringOption = Some[String]("bla")
      stringOption.map(s => s.toUpperCase) mustEqual Some[String]("BLA")

      val none:Option[String] = None
      none.map(s => s.toUpperCase) mustEqual None
    }
  }

}
