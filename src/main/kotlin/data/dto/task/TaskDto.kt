package data.dto.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    @SerialName("_id") val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val taskState: String,
    val createdBy: String,
    val taskLogs: List<String>,
)