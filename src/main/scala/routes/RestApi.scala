package routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import cats.data.Xor
import db.repositories.ToDoRepository
import models.{ToDo, TodoParser, TodoToCreate}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class RestApi(repo: ToDoRepository)(implicit ec: ExecutionContext) {
  import models.JsonProtocol._

  private val todoPostRoute = post {
    path("todo") {
        entity(as[String]) { body =>
          TodoParser.toToDo(body) match {
            case Xor.Left(_) => complete(StatusCodes.BadRequest)
            case Xor.Right(todo) =>
              onComplete(repo.create(todo)) {
                case Success(_) => complete(StatusCodes.Created)
                case Failure(ex) => complete(StatusCodes.BadRequest)
          }
        }
      }
    }
  }

  private val todoGetRoute = get {
    path("todo") {
      onComplete(repo.getAll) {
        case Success(listOfTodos) => complete(listOfTodos)
        case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
      }
    } ~
      path("todo" / Segment) { (uuid: String) =>
        onComplete(repo.getById(uuid)) {
          case Success(todoOpt) => todoOpt match {
            case Some(todo: ToDo) => complete(todo)
            case None => complete(NotFound, s"The todo doesn't exist")
          }
          case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
  }

  private val todoPutRoute = put {
    path("todo" / Segment) { (uuid: String) =>
      entity(as[TodoToCreate]) { todoToUpdate =>
        onComplete(repo.update(ToDo(uuid, todoToUpdate.title, todoToUpdate.description, todoToUpdate.dueDate))) {
          case Success(todoOpt) => complete(StatusCodes.OK)
          case Failure(ex) => complete(StatusCodes.BadRequest)
        }
      }
    }
  }

  private val todoDeleteRoute =
    delete {
        path("todo" / Segment) { (uuid: String) =>
          onComplete(repo.delete(uuid)) {
            case Success(todo) => complete(StatusCodes.NoContent)
            case Failure(ex) => complete(StatusCodes.InternalServerError)
          }
        }
      }

  val routes: Route = todoGetRoute ~ todoPostRoute ~ todoPutRoute ~ todoDeleteRoute
}
