package service

import java.util.UUID

import models.ToDo

import scala.util.Try

trait ToDoService {
  def create(todo: ToDo): Try[ToDo]
  def getAll: List[ToDo]
  def getById(id: UUID): Option[ToDo]
  def update(todo: ToDo): Try[ToDo]
}
