package crosscutting.transferobject.lesson

import crosscutting.basetype.Id
import org.joda.time.DateTime
import crosscutting.transferobject.base.PersistableTransferObject

case class Lesson(override val id:Id, start:DateTime, end:DateTime, teacherId:Id /*, studentIds:List[Id] */ )
  extends PersistableTransferObject(id)

