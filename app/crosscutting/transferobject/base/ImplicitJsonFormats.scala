package crosscutting.transferobject.base

import play.api.libs.json.Json
import crosscutting.transferobject.person.{Student, Teacher, Person, Address}
import crosscutting.transferobject.lesson.Lesson

object ImplicitJsonFormats {

  implicit val addressFormat = Json.format[Address]
  implicit val personFormat = Json.format[Person]
  implicit val lessonFormat = Json.format[Lesson]

}
