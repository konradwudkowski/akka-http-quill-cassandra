package routes

import java.util.UUID
import java.util.UUID.randomUUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import db.repositories.{ToDoRepository, ToDoRepositoryImpl}
import models.ToDo
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future
import scala.util.Try

class RestApiSpec extends WordSpec with Matchers with ScalatestRouteTest {

  val idOfExistingToDo = randomUUID()
  val router: RestApi = new RestApi(new ToDoRepository {
    override def update(todo: ToDo): Future[ToDo] = ???

    override def getById(id: String): Future[Option[ToDo]] = ???

    override def create(todo: ToDo): Future[ToDo] = ???

    override def getAll: Future[List[ToDo]] = ???

    override  def delete(id: String): Future[Unit] = ???
  })

  "POST /todo" should {

    "return a 400 if the body is not a valid ToDo request" in {
      Post("/todo", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    "return 201 if ToDo was created" in {
      Post("/todo", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.Created
      }
    }

  }

  "GET /todo" should {

    "return a 200 and the list of ToDo's" in {
      Get("/todo", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }

  }

  "GET /todo/:id" should {

    "reject a request if ID is not an UUID" in {
      Get("/todo/1", "") ~> router.routes ~> check {
        rejections.size shouldBe 0
      }
    }

    "return 404 if ToDo doesn't exist" in {
      Get(s"/todo/$randomUUID", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "return 200 and the ToDo, if it exists" in {
      Get(s"/todo/$idOfExistingToDo", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }

  }

  "PUT /todo/:id" should {

    "return 404 if ToDo doesn't exist" in {
      Put(s"/todo/$randomUUID", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "return 200 and the ToDo, if it exists" in {
      Put(s"/todo/$idOfExistingToDo", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }

  }

  "DELETE /todo/:id" should {

    "return 404 if ToDo doesn't exist" in {
      Delete(s"/todo/$randomUUID", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "return 204 and the ToDo, if it exists" in {
      Delete(s"/todo/$idOfExistingToDo", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.NoContent
      }
    }

  }
}
