package models

import slick.driver.ExtendedProfile

class DAL(override val profile: ExtendedProfile) extends UserComponent with ToDoComponent with Profile {

  import profile.simple._

  def create(implicit session: Session): Unit = {
    Users.ddl.create //helper method to create all tables
    ToDos.ddl.create
  }
}
