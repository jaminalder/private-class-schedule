package dataaccess.lesson

import dataaccess.base.DataAccessObject
import crosscutting.transferobject.lesson.Lesson

object LessonDataAccessObject extends DataAccessObject[Lesson] {

    override val collectionName = "lesson"

    override def converter = LessonDBObjectConverter

}
