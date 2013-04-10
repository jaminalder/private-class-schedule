package service.id

import play.api.mvc._
import domain.base.ID
import play.api.libs.json.Json

/**
 * JSON Service.
 * Responsible for UUID delivery to the UI.
 */
object IDService extends Controller {

  /**
   * Yields a new random UUID.
   */
  def generate = Action {
    Ok(Json.obj("_id" -> ID.generate))
  }

}
