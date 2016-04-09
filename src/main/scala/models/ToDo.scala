package models

case class ToDo(id: String,
                title: String,
                description: String,
                dueDate: String)

case class TodoToCreate(title: String,
                        description: String,
                        dueDate: String)
