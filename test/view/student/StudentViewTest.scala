package view.student

import org.specs2.mutable._
import play.api.test._
import org.fluentlenium.core.domain.{FluentWebElement, FluentList}

class StudentViewTest extends Specification {


  sequential

  "Student View" should {
    "create a new student" in new WithBrowser(webDriver = Helpers.FIREFOX) {
      browser.goTo("/app/index.html#/student")
      //browser.await()
      //println("student button: " + browser.$("#newStudentButton"))
      val cont: FluentList[FluentWebElement] = browser.$(".container")
      println("container: ")

      println("container values: " + browser.$(".container").getValues)


      failure

      /*
      browser.$("a").click()

      browser.url must equalTo("/")
      browser.$("#title").getTexts().get(0) must equalTo("Hello Coco")
      */
    }
  }

}
