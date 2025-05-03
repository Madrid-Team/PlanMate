package data.dto.task

import java.util.UUID


data class TaskDto(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val taskState: String,
    val createdBy: String,
    val logs: List<String>,
)
