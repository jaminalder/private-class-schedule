package scalatests

import org.specs2.mutable._
import scala.collection.mutable.Map

class CakeTest extends Specification {

  "domain objects" should {
    "be extensible with data access objects" in {

      import TestComponent.TestDomain
      val domainObject = TestDomain(1, "blabla", "blibli")
      TestDomain.getById(1) must beNone
      domainObject.save
      TestDomain.getById(1) must beSome[TestDomain]
      TestDomain.getById(1).get mustEqual domainObject



    }
  }

}

object TestComponent
  extends TestDomainComponent
  with TestDAOComponent
  with TestConverterComponent {

  val dao = TestDataAccessObject
  val converter = TestConverter

}


trait DomainComponent {
  self: DAOComponent =>

  type DomainObjectType <: DomainObject

  trait DomainObject {
    self: DomainObjectType =>
    def save = dao.save(this)
  }

  trait DomainCompanionObject {
    def getById(id:Int):Option[DomainObjectType] = dao.getById(id)
  }
}

trait TestDomainComponent extends DomainComponent {
  self: TestDAOComponent =>

  type DomainObjectType = TestDomain
  case class TestDomain(id:Int, bla:String, bli:String) extends DomainObject
  object TestDomain extends DomainCompanionObject

}

trait DAOComponent {
  self: DomainComponent with ConverterComponent =>

  val dao: AbstractDataAccessObject[DomainObjectType]

  trait AbstractDataAccessObject[DO <: DomainObjectType] {
    val db: Map[Int, List[String]]
    def save(domainObject:DO)
    def getById(id:Int):Option[DO]
  }
}


trait TestDAOComponent extends DAOComponent {
  self: TestDomainComponent with TestConverterComponent =>

  object TestDataAccessObject extends AbstractDataAccessObject[TestDomain] {
    val db = Map[Int, List[String]]()
    override def save(domainObject:TestDomain) = db += (domainObject.id -> converter.fromDomainObjectToDataObject(domainObject))
    override def getById(id:Int):Option[TestDomain] = db.get(id).map(converter.fromDataObjectToDomainObject)
  }


}

trait ConverterComponent {
  self:DomainComponent =>

  val converter: AbstractTestConverter[DomainObjectType]

  trait AbstractTestConverter[DO <: DomainObjectType] {
    def fromDomainObjectToDataObject(domainObject: DO): List[String]
    def fromDataObjectToDomainObject(dataObject: List[String]): DO
  }
}

trait TestConverterComponent extends ConverterComponent {
  self: TestDomainComponent =>

  object TestConverter extends AbstractTestConverter[TestDomain] {
    override def fromDomainObjectToDataObject(domainObject: TestDomain): List[String] =
      List(domainObject.id.toString, domainObject.bla, domainObject.bli)

    override def fromDataObjectToDomainObject(dataObject: List[String]): TestDomain =
      TestDomain(dataObject(0).toInt, dataObject(1), dataObject(2))
  }
}




