package models

import java.time.ZonedDateTime
import java.util.UUID

case class ToDo(id: UUID,
                title: String,
                description: String,
                dueDate: String)
