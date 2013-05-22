package crosscutting.basetype

import java.util.UUID

case class Id(_id:String)

object Id {
  val rootID = Id("42")
  def generate = Id(UUID.randomUUID().toString)

  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  implicit val idFormat = (__ \ '_id).format[String].inmap(  (l: String) => Id(l), (a: Id) => a._id )
}


