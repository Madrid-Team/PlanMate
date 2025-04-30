package data.dto.project

import java.util.*

data class ProjectDto(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val createdBy: String,
    val projectLogs: List<String>,
    val projectState: String,
    val taskStates: List<String>,
)