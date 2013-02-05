package models

case class ToDo(id: Option[String], text:String, done:Boolean)

trait ToDoComponent {
  this: Profile =>

  import profile.simple._

  object ToDos extends Table[ToDo]("todos") {
    def id = column[String]("id", O.PrimaryKey)
    def text =  column[String]("text", O.NotNull)
    def done =  column[Boolean]("done", O.NotNull)
    def * = id.? ~ text ~ done <> (ToDo, ToDo.unapply _)

    def add(todo : ToDo)(implicit session: Session) = {
      this.insert(todo)
    }

    def getAll(implicit session: Session) = {
      (for {
        todo <- ToDos
      } yield(todo)).list
    }
  }
}