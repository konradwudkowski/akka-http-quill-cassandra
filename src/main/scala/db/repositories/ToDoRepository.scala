package db.repositories

import java.util.UUID

import io.getquill.naming.Literal
import io.getquill.sources.cassandra.{CassandraAsyncSource, CassandraSyncSource}
import models.ToDo

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

import io.getquill._

trait ToDoRepository {
  def create(todo: ToDo): Future[ToDo]
  def delete(id: UUID): Future[Unit]
  def getAll: Future[List[ToDo]]
  def getById(id: UUID): Future[Option[ToDo]]
  def update(todo: ToDo): Future[ToDo]
}

class ToDoRepositoryImpl(cassandra: CassandraAsyncSource[Literal])(implicit ec: ExecutionContext) extends ToDoRepository {

  // ifNotExists can't be used here due to the type, UnassignedAction[_] with Insert[_]
  override def create(todo: ToDo): Future[ToDo] =
    cassandra.run(query[ToDo].insert)(todo).map(_ => todo)

  override def delete(id: UUID): Future[Unit] = {
    val q = quote {
      (todoId: UUID) => query[ToDo].filter(_.id == todoId).delete
    }
    cassandra.run(q)(id).map(_ => ())
  }

  // ifExists can't be used here due to the type, UnassignedAction[_] with Insert[_]
  override def update(todo: ToDo): Future[ToDo] =
    cassandra.run(query[ToDo].update)(todo).map(_ => todo)

  override def getById(id: UUID): Future[Option[ToDo]] = {
    val q = quote {
      (todoId: UUID) => query[ToDo].filter(_.id == todoId)
    }
    cassandra.run(q)(id).map(_.headOption)
  }

  override def getAll: Future[List[ToDo]] =
    cassandra.run(query[ToDo])
}
