package models

import java.util.UUID

import cats.data.Xor.Right
import io.circe.Json
import org.scalatest.{FreeSpec, FunSuite, Matchers}

/**
  * Created by iyadi on 09/04/2016.
  */
class TodoParserSpec extends FreeSpec with Matchers{

  "Parser should parse correctly" in {

    val title = "title"
    val desc = "description"
    val dueDate = "1970/01/01"
    val inputJson =
      s"""
      {"title":"$title", "description":"$desc", "dueDate": "$dueDate"}
      """
    val actual:ToDo = TodoParser.toToDo(inputJson) match {
      case Right(t) => t
    }

    actual.id.isEmpty shouldBe false
    actual.title shouldBe title
    actual.description shouldBe desc
    actual.dueDate shouldBe dueDate


  }



}
