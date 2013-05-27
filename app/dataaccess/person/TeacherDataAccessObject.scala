package dataaccess.person

import crosscutting.transferobject.person.Teacher

object TeacherDataAccessObject extends RoleDataAccessObject[Teacher] {
  def converter = TeacherDBObjectConverter
}
