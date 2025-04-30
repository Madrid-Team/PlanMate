package data.dto.task

import java.util.UUID


data class TaskDto(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val title: String,
    val description: String,
    val state: String,
    val createdBy: String,
    val logs: List<String>,
)
