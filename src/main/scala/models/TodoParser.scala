package models


import io.circe.{Error, Json}
import java.util.UUID

import cats.data.Xor
import io.circe.generic.auto._
import io.circe.parser._

object TodoParser {
  /**
    A utility method that parses a json string and returns a TODO object with
    a UUID added to the JSON object
   */
  def toToDo(json: String): Xor[Error, ToDo] = {
    val idValue = Json.fromString(UUID.randomUUID().toString)
    parse(json).map(json => json.mapObject(obj => obj.add("id", idValue))).flatMap(obj => obj.as[ToDo])
  }

}
