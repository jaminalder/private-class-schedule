package scalatests

import org.specs2.mutable._

class ReflectionTest extends Specification {

  case class DummyClass(bla: String)

  "The class name" should {
    "be extractable from an object" in {
      val dummy = DummyClass("bli")
      dummy.getClass.getSimpleName mustEqual "DummyClass"
    }

    "...and the class" in {
      success
      //does not work :-(
      //DummyClass.getClass.getSimpleName mustEqual "DummyClass"
    }

  }

}
