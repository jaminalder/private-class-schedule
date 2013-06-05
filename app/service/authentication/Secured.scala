package service.authentication

import play.api.mvc._

trait Secured {

  /**
   * Retrieve the connected users id
   */
  private def username(request: RequestHeader) = request.session.get("userId")

  /**
   * Response forbidden if the user is not logged in
   */
  private def onUnauthorized(request: RequestHeader) = Results.Unauthorized

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }

}
