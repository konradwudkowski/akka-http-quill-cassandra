package models

import java.util.UUID

case class ToDo(id: UUID,
                title: String,
                description: String,
                dueDate: String)

case class TodoToCreate(title: String,
                        description: String,
                        dueDate: String)