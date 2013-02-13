package models

import java.util.UUID

case class ToDo(id: Option[String], text: String, done: Boolean)

trait ToDoComponent {
  this: Profile =>

  import profile.simple._

  object ToDos extends Table[ToDo]("todos") {
    def id = column[String]("id", O.PrimaryKey)
    def text = column[String]("text", O.NotNull)
    def done = column[Boolean]("done", O.NotNull)

    def * = id.? ~ text ~ done <>(ToDo, ToDo.unapply _)

    def todosidx1 = index("todosidxtext", text, unique = true)

    def persist(todo: ToDo)(implicit session: Session): ToDo = {
      todo.id match {
        case None => {
          val todoWithID = todo.copy(id = Some(UUID.randomUUID().toString()))
          this.insert(todoWithID)
          return todoWithID
        }
        case Some(id) => {
          val updateQuery = for (t <- ToDos if t.id is id) yield t
          updateQuery.update(todo)
          return todo
        }
      }
    }

    def delete(id: String)(implicit session: Session) = {
      val updateQuery = for (t <- ToDos if t.id is id) yield t
      updateQuery.delete
    }

    def getAll(implicit session: Session) = {
      (for {
        todo <- ToDos
      } yield (todo)).list
    }
  }

}