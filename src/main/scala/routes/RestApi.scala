package routes

import java.time.ZonedDateTime
import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import models.ToDo
import service.ToDoService

import scala.util.{Failure, Success}

class RestApi(service: ToDoService) {
  val routes =
    post {
      path("todo") {
        complete {
          service.create(ToDo(UUID.randomUUID(), "", "", "")) match {
            case Success(todo) => StatusCodes.Created
            case Failure(ex) => StatusCodes.BadRequest
          }
        }
      }
    } ~
      get {
        path("todo") {
          complete(StatusCodes.BadRequest, "bohoo")
        } ~
          path("todo" / JavaUUID) { (uuid: UUID) =>
            complete(StatusCodes.BadRequest, "bohoo")
          }
      } ~
      put {
        path("todo" / JavaUUID) { (uuid: UUID) =>
          complete(StatusCodes.BadRequest, "bohoo")
        }
      } ~
      delete {
        path("todo" / JavaUUID) { (uuid: UUID) =>
          complete(StatusCodes.BadRequest, "bohoo")
        }
      }
}
