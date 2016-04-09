package service

import java.util.UUID

import io.getquill._
import io.getquill.naming.SnakeCase
import models.ToDo
import utils.ConfigurationModuleImpl

import scala.concurrent.Future
import scala.util.Try

trait ToDoService {
  def create(todo: ToDo): Future[ToDo]
  def getAll: Future[List[ToDo]]
  def getById(id: UUID): Future[Option[ToDo]]
  def update(todo: ToDo): Future[ToDo]
  def delete(id: UUID): Future[ToDo]
}

class ToDoServiceImpl extends ToDoService with ConfigurationModuleImpl {
  lazy val db = source(new CassandraAsyncSourceConfig[SnakeCase]("cassandradb"))

  def create(todo: ToDo): Try[ToDo] = null
  def getAll: List[ToDo] = null
  def getById(id: UUID): Future[Option[ToDo]] = null
  def update(todo: ToDo): Try[ToDo] = null
  def delete(id: UUID): Future[ToDo] = null
}