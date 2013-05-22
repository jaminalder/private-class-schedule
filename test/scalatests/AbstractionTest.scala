package scalatests

import org.specs2.mutable._
import scala.collection.mutable.Map

class LayeringTest extends Specification {

  "domain objects" should {
    "be extensible with data access objects" in {
      val domainObject = ConcreteTestDomainObject(1, "blabla", "blibli")
      domainObject.db.get(1) must beNone
      domainObject.save
      domainObject.db.get(1) must beSome[List[String]]
      domainObject.db.get(1).get mustEqual List("1", "blabla", "blibli")
      domainObject.getById(1) must beSome[ConcreteTestDomainObject]
      domainObject.getById(1).get mustEqual domainObject

    }
  }

}

abstract class AbstractTestDomainObject(val id: Int){
  override def hashCode(): Int = id

  override def equals(obj: Any): Boolean = obj match {
    case other: AbstractTestDomainObject => id == other.id
    case _ => false
  }
}

class ConcreteTestDomainObject(override val id: Int, val bla: String, val bli: String) extends AbstractTestDomainObject(id)


object ConcreteTestDomainObject {
  type DAO = ConcreteTestDAO

  def apply(id:Int, bla: String, bli: String) = new ConcreteTestDomainObject(id, bla, bli) with DAO
  def apply(bla:String, bli:String) = new ConcreteTestDomainObject((Math.random()*100).toInt, bla, bli) with DAO

}


abstract class AbstractTestConverter[DomainObject, DataObject] {
  def fromDomainObjectToDataObject(domainObject: DomainObject): DataObject

  def fromDataObjectToDomainObject(dataObject: DataObject): DomainObject
}

abstract class AbstractTestDBConverter[DomainObject] extends AbstractTestConverter[DomainObject, List[String]]

object ConcreteTestConverter extends AbstractTestDBConverter[ConcreteTestDomainObject] {
  def fromDomainObjectToDataObject(domainObject: ConcreteTestDomainObject): List[String] =
    List(domainObject.id.toString, domainObject.bla, domainObject.bli)

  def fromDataObjectToDomainObject(dataObject: List[String]): ConcreteTestDomainObject =
    ConcreteTestDomainObject(dataObject(0).toInt, dataObject(1), dataObject(2))
}

trait AbstractTestDAO[Converter[DO] <: AbstractTestDBConverter[DO]] {
  type DomainObject <: AbstractTestDomainObject
  val thisDo: DomainObject
  val converter: Converter[DomainObject]

  val db = Map[Int, List[String]]()

  def save = db += (thisDo.id -> converter.fromDomainObjectToDataObject(thisDo))

  def getById(myId: Int): Option[DomainObject] = db.get(myId).map(converter.fromDataObjectToDomainObject)

  def printDB = println("test db: " + db.toString)


}

trait ConcreteTestDAO extends AbstractTestDAO[AbstractTestDBConverter] {
  self: ConcreteTestDomainObject =>
  type DomainObject = ConcreteTestDomainObject
  val thisDo = self
  val converter = ConcreteTestConverter

}



