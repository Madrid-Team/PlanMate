package domain.models.project

import domain.models.logs.AuditLog
import domain.models.task.Task
import java.util.*

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val createdBy: String,
    val projectLogs: List<AuditLog>,
    val projectState: String,
    val taskStates: List<String>,
)