package service.id

import org.specs2.mutable._
import play.api.test.WithServer
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._
import dataaccess.person.{PersonTestData, PersonDAO}
import play.api.libs.ws.WS.WSRequestHolder
import conversion.json.PersonJsonConverter._
import domain.person.Person
import play.api.libs.json.JsArray

class IDServiceTest extends Specification {

  "IDService" should {

    "generate UUIDs" in new WithServer {

      val requestHolder = WS.url("http://localhost:19001/api/id/generate")
      val response1: Response = await(requestHolder.get)
      response1.status must equalTo(OK)
      val response2: Response = await(requestHolder.get)
      response2.status must equalTo(OK)
      response1.body.size mustEqual 36
      response2.body.size mustEqual 36
      response1.body mustNotEqual response2.body

    }


  }
}
