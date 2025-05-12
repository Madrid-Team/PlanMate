package data.dto.project

import data.dto.task.TaskDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProjectDto(
    @SerialName("_id") val id: String,
    val name: String,
    val description: String,
    val createdBy: String,
    val projectLogs: List<String>,
    val projectState: String,
    val taskStates: List<String>,
    val projectStates: List<String>,
    val matesUsernames:List<String> = emptyList(),
    val tasks:List<TaskDto> = emptyList()
)