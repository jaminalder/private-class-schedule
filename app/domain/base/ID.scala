package domain.base

import java.util.UUID

object ID {
  def generate: String = UUID.randomUUID().toString()
}

trait ID {
  def _id: String
}
