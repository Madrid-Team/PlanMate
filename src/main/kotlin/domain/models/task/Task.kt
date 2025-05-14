package domain.models.task

import java.util.*


data class Task(
    val id: UUID,
    val projectId: String,
    val title: String,
    val description: String,
    val taskState: String,
    val createdBy: String,
    val taskLogs: List<String>,
)
