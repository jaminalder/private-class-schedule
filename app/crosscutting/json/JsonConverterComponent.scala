package crosscutting.json

import domain.base.StorableDomainComponent
import play.api.libs.json.{Format, Json, JsValue}

trait JsonConverterComponent {
  self: StorableDomainComponent =>

  implicit val format: Format[StorableDomainObjectType]

  val jsonConverter = JsonConverter

  object JsonConverter {
    def domainToJson(domainObject: StorableDomainObjectType):JsValue = Json.toJson(domainObject)
    def jsonToDomain(jsValue: JsValue):StorableDomainObjectType = jsValue.as[StorableDomainObjectType]
    def jsonToDomain(jsStringValue: String):StorableDomainObjectType = Json.parse(jsStringValue).as[StorableDomainObjectType]
  }

}
