package service.id

import play.api.mvc._
import play.api.libs.json.Json
import crosscutting.basetype.Id

/**
 * JSON Service.
 * Responsible for UUID delivery to the UI.
 */
object IDService extends Controller {

  /**
   * Yields a new generate UUID.
   */
  def generate = Action {
    Ok(Json.toJson(Id.generate))
  }

}
