package models

import java.util.UUID

import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat}

object JsonProtocol extends DefaultJsonProtocol {

  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(x: UUID) = JsString(x.toString)
    def read(value: JsValue) = value match {
      case JsString(x) => UUID.fromString(x)
      case x => throw new Error("Expected UUID as JsString, but got " + x)
    }
  }

  implicit val todoFormat = jsonFormat4(ToDo)
  implicit val todoToCreateFormat: RootJsonFormat[TodoToCreate] = jsonFormat3(TodoToCreate)
}
