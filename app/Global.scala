import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import dataaccess.person.PersonDAO
import domain.base.ID
import domain.person.{Address, Person}
import domain.role.Teacher
import play.api._

object Global extends GlobalSettings {

  val dummyUserEMail: String = "dummy.user@email.com"
  val dummyUser = {
    Teacher(Person(_id = ID.generate, lastName = "User", firstName = "Dummy", eMail = dummyUserEMail,
      address = Address(street = "street", streetNum = "3", city = "Bern", zip = "8000"), ownerID = ID.rootID))
  }

  override def onStart(app: Application) {
    Logger.info("### Application startup... ###")

    RegisterJodaTimeConversionHelpers()

    //PersonDAO.collection.drop
    if(PersonDAO.getByEMail(dummyUserEMail).isEmpty) {
      PersonDAO.persist(dummyUser)
    }
    println("### all persons in db:")
    PersonDAO.collection.find.foreach(println)
    Logger.info("### Application startup finished ###")
  }

  override def onStop(app: Application) {
    Logger.info("### Application shutdown...")
  }

}