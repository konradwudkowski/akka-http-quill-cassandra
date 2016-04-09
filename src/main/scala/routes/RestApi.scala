package routes

import java.time.ZonedDateTime
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import io.getquill._
import io.getquill.naming.SnakeCase
import models.{TodoToCreate, ToDo}
import service.ToDoService

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.util.{Failure, Success}

class RestApi(service: ToDoService)(implicit ec: ExecutionContext) {
  import models.JsonProtocol._

  private val todoPostRoute = post {
    path("todo") {
        entity(as[TodoToCreate]) { todoToCreate =>
          onComplete(service.create(ToDo(UUID.randomUUID(), todoToCreate.title, todoToCreate.description, todoToCreate.dueDate))) {
            case Success(todo) => complete(StatusCodes.Created)
            case Failure(ex) => complete(StatusCodes.BadRequest)
        }
      }
    }
  }

  private val todoGetRoute = get {
    path("todo") {
      complete(StatusCodes.BadRequest, "bohoo")
    } ~
      path("todo" / JavaUUID) { (uuid: UUID) =>
        onComplete(service.getById(uuid)) {
          case Success(todoOpt) => todoOpt match {
            case Some(todo: ToDo) => complete(todo)
            case None => complete(NotFound, s"The todo doesn't exist")
          }
          case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
  }

  private val todoPutRoute = put {
    path("todo" / JavaUUID) { (uuid: UUID) =>
      entity(as[TodoToCreate]) { todoToUpdate =>
        onComplete(service.update(ToDo(uuid, todoToUpdate.title, todoToUpdate.description, todoToUpdate.dueDate))) {
          case Success(todoOpt) => complete(StatusCodes.OK)
          case Failure(ex) => complete(StatusCodes.BadRequest)
        }
      }
    }
  }

  private val todoDeleteRoute =
    delete {
        path("todo" / JavaUUID) { (uuid: UUID) =>
          onComplete(service.delete(uuid)) {
            case Success(todo) => complete(StatusCodes.Created)
            case Failure(ex) => complete(StatusCodes.BadRequest)
          }
        }
      }

  val routes: Route = todoGetRoute // ~ todoPostRoute ~ todoPutRoute ~ todoDeleteRoute
}
