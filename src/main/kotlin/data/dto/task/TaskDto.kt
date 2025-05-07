package data.dto.task

import data.dto.authentication.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val taskState: String,
    val createdBy: String,
    val logs: List<String>,
)