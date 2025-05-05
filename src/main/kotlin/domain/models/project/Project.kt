package domain.models.project

import java.util.*

data class Project(
    val id: UUID,
    val name: String,
    val description: String,
    val createdBy: String,
    val projectLogs: List<String>,
    val projectState: String,
    val taskStates: List<String>,
    val projectStates: List<String>,
)