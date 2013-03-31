package domain.base

import java.util.UUID

object ID {
  val rootID: String = "42"
  def generate: String = UUID.randomUUID().toString()
}

trait ID {
  def _id: String
}
