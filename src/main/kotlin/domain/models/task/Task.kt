package domain.models.task

import domain.models.logs.AuditLog
import java.util.UUID


data class Task(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val title: String,
    val description: String,
    val state: String,
    val createdBy: String,
    val logs: List<String>,
)
