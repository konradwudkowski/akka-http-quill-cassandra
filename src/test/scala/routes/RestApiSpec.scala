package routes

import java.util.UUID.randomUUID

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import db.repositories.ToDoRepository
import models.ToDo
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RestApiSpec extends WordSpec with Matchers with ScalatestRouteTest {

  val idOfExistingToDo = randomUUID().toString

  class TestRepository extends ToDoRepository {
    def update(todo: ToDo): Future[ToDo] = ???
    def getById(id: String): Future[Option[ToDo]] = ???
    def create(todo: ToDo): Future[ToDo] = ???
    def getAll: Future[List[ToDo]] = ???
    def delete(id: String): Future[Unit] = ???
  }

  val router: RestApi = new RestApi(new TestRepository)

  "POST /todo" should {
    "return a 400 if the body is not a valid ToDo request" in {
      val api = new RestApi(new TestRepository)
      Post("/todo", "") ~> api.routes ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }
    "return 201 if ToDo was created" in {
      val api = new RestApi(new TestRepository() {
        override def create(todo: ToDo): Future[ToDo] = Future.successful(todo)
      })
      val validJson = """ {"title":"title", "description":"desc", "dueDate": "dueDate"} """
      Post("/todo", validJson) ~> api.routes ~> check {
        status shouldBe StatusCodes.Created
      }
    }

  }

  "GET /todo" should {
    "return a 200 and the list of ToDo's" in {
      val api = new RestApi(new TestRepository() {
        override def getAll: Future[List[ToDo]] = Future.successful(List())
      })
      Get("/todo", "") ~> api.routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }
  }

  "GET /todo/:id" should {
    "reject a request if ID is not an UUID" in {
      pending
      Get("/todo/1", "") ~> router.routes ~> check {
        rejections.size shouldBe 0
      }
    }
    "return 404 if ToDo doesn't exist" in {
      val api = new RestApi(new TestRepository() {
        override def getById(id: String): Future[Option[ToDo]] = Future.successful(None)
      })
      Get(s"/todo/$randomUUID", "") ~> api.routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }
    "return 200 and the ToDo, if it exists" in {
      val api = new RestApi(new TestRepository() {
        override def getById(id: String): Future[Option[ToDo]] = Future.successful(Some(ToDo(idOfExistingToDo, "", "", "")))
      })
      Get(s"/todo/$idOfExistingToDo", "") ~> api.routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }

  }

  "PUT /todo/:id" should {
    "return 404 if ToDo doesn't exist" in {
      pending
      Put(s"/todo/$randomUUID", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }
    "return 200 and the ToDo, if it exists" in {
      val api = new RestApi(new TestRepository() {
        override def update(todo: ToDo): Future[ToDo] = Future.successful(todo)
      })
      val validJson = """ {"title":"title", "description":"desc", "dueDate": "dueDate"} """
      val request = HttpEntity(ContentTypes.`application/json`, validJson)
      Put(s"/todo/$idOfExistingToDo", request) ~> api.routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }
  }

  "DELETE /todo/:id" should {
    "return 404 if ToDo doesn't exist" in {
      pending
      Delete(s"/todo/$randomUUID", "") ~> router.routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }
    "return 204 and the ToDo, if it exists" in {
      val api = new RestApi(new TestRepository() {
        override def delete(id: String): Future[Unit] = Future.successful((): Unit)
      })
      Delete(s"/todo/$idOfExistingToDo", "") ~> api.routes ~> check {
        status shouldBe StatusCodes.NoContent
      }
    }

  }
}
