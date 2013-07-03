package service.authentication

import play.api.libs.ws.WS
import play.api.mvc.{Session, Cookies}
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.libs.ws.Response
import play.api.mvc.Cookie
import play.api.libs.ws.WS.WSRequestHolder

object AuthenticationTestHelper {

  def getValueFromSessonCookie(response: Response, key: String): Option[String] = {
    val playSession: String = response.header("Set-Cookie").getOrElse(return None)
    val decodedCookies: Seq[Cookie] = Cookies.decode(playSession)
    val sessionCookie: Option[Cookie] = decodedCookies.find(c => c.name.equals("PLAY_SESSION"))
    val decodedSession: Session = Session.decodeFromCookie(sessionCookie)
    val valueInSession = decodedSession.get(key)
    return valueInSession
  }

  def testLoginUserAsTeacher(eMail: String, password: String): Response = {
    val jsonInput = Json.obj("eMail" -> eMail, "password" -> password)
    val requestHolder = WS.url("http://localhost:19001/api/authentication/loginUserAsTeacher")
    await(requestHolder.post(jsonInput))
  }

  def callServiceWithUserId(url: String, userId: String): Response = {
    val sessionCookie: Cookie = Session.encodeAsCookie(Session(Map("userId" -> userId)))
    val loggedInUserRequestHolder: WSRequestHolder = WS.url(url)
      .withHeaders(play.api.http.HeaderNames.COOKIE -> Cookies.encode(Seq(sessionCookie)))
    return await(loggedInUserRequestHolder.get)

  }

  def requestHolderWithUserId(url: String, userId: String): WSRequestHolder = {
    val sessionCookie: Cookie = Session.encodeAsCookie(Session(Map("userId" -> userId)))
    return WS.url(url).withHeaders(play.api.http.HeaderNames.COOKIE -> Cookies.encode(Seq(sessionCookie)))
  }

}
