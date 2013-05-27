package scalatests

import org.specs2.mutable._
import scala.collection.mutable.Map

class CakeTest2 extends Specification {

  "domain objects" should {
    "be extensible with data access objects" in {

      val domainObject = TestDomain(1, "blabla", "blibli")
      TestDomain.getById(1) must beNone
      TestDomain.save(domainObject)
      TestDomain.getById(1) must beSome[TestDomain]
      TestDomain.getById(1).get mustEqual domainObject


    }
  }

}

object TestDomain extends TestDomainComponent{
  val dao = TestDataAccessObject
}

abstract class PersistableTransferObject(val id:Int)
case class TestDomain(override val id: Int, bla: String, bli: String) extends PersistableTransferObject(id)

trait TestDomainComponent {

  val dao: DataAccessObject[TestDomain]

  def getById(id: Int): Option[TestDomain] = dao.getById(id)
  def save(to: TestDomain) = dao.save(to)

}

trait DataAccessObject[TO <: PersistableTransferObject] {
  val db: Map[Int, List[String]]

  def converter: DataObjectConverter[TO]

  def save(domainObject: TO)

  def getById(id: Int): Option[TO]
}

object TestDataAccessObject extends DataAccessObject[TestDomain] {
  val db = Map[Int, List[String]]()

  def converter = TestConverter

  override def save(domainObject: TestDomain) = db += (domainObject.id -> converter.fromDomainObjectToDataObject(domainObject))

  override def getById(id: Int): Option[TestDomain] = db.get(id).map(converter.fromDataObjectToDomainObject)
}

trait DataObjectConverter[TO <: PersistableTransferObject] {

  def fromDomainObjectToDataObject(domainObject: TO): List[String]

  def fromDataObjectToDomainObject(dataObject: List[String]): TO
}

object TestConverter extends DataObjectConverter[TestDomain] {

  override def fromDomainObjectToDataObject(domainObject: TestDomain): List[String] =
    List(domainObject.id.toString, domainObject.bla, domainObject.bli)

  override def fromDataObjectToDomainObject(dataObject: List[String]): TestDomain =
    TestDomain(dataObject(0).toInt, dataObject(1), dataObject(2))
}




