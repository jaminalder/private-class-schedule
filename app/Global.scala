import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import crosscutting.basetype.Id
import crosscutting.transferobject.person.{Address, Person, Teacher}
import play.api._
import service.WiringModule.TeacherDomainComponent

object Global extends GlobalSettings {

  val dummyUserEMail: String = "dummy.user@email.com"
  val dummyUser = {
    Teacher(Person(id = Id.generate, lastName = "User", firstName = "Dummy", eMail = dummyUserEMail,
      address = Address(street = "street", streetNum = "3", city = "Bern", zip = "8000"), ownerID = Id.rootID))
  }

  override def onStart(app: Application) {
    Logger.info("### Application startup... ###")

    RegisterJodaTimeConversionHelpers()

    //PersonDAO.collection.drop
    if(TeacherDomainComponent.getTeacherByEmail(dummyUserEMail).isEmpty) {
      TeacherDomainComponent.saveTeacher(dummyUser)
    }
    println("### all persons in db:")
    TeacherDomainComponent.dao.collection.find.foreach(println)
    Logger.info("### Application startup finished ###")
  }

  override def onStop(app: Application) {
    Logger.info("### Application shutdown...")
  }

}