package routes

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.Directives._
import org.scalatest.{Matchers, WordSpec}

class RestApiSpec extends WordSpec with Matchers with ScalatestRouteTest {

  val route =
    post {
      path("todo") {
        complete {
          (StatusCodes.NotFound, "bohoo")
        }
      }
    } ~
      get {
        path("todo" / JavaUUID) { (uuid: UUID) =>
          complete(StatusCodes.BadRequest, "bohoo")
        }
      }

  "POST /todo" should {

    "return a 400 if the body is not a valid ToDo request" in {
      Post("/todo", "") ~> route ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    "return 201 if ToDo was created" in {
      Post("/todo", "") ~> route ~> check {
        status shouldBe StatusCodes.Created
      }
    }
  }

  "GET /todo" should {

    "return a 200 and the list of ToDo's" in {
      Get("/todo", "") ~> route ~> check {
        status shouldBe StatusCodes.OK
      }
    }
  }

  "GET /todo/:id" should {

    "reject a request if ID is not an UUID" in {
      Get("/todo/1", "") ~> route ~> check {
        rejections.size shouldBe 0
      }
    }

    "return 404 if ToDo doesn't exist" in {
      Get("/todo/1", "") ~> route ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "return 200 and the ToDo, if it exists" in {
      Get("/todo/1", "") ~> route ~> check {
        status shouldBe StatusCodes.OK
      }
    }

  }

  "PUT /todo/:id" should {

    "return 404 if ToDo doesn't exist" in {
      Put("/todo/1", "") ~> route ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "return 200 and the ToDo, if it exists" in {
      Get("/todo/1", "") ~> route ~> check {
        status shouldBe StatusCodes.OK
      }
    }

  }
}
