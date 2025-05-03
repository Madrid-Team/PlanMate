package presentation.feature.projects

import domain.models.project.Project
import java.util.UUID

fun helperProject(
    id: String = UUID.randomUUID().toString(),
    name: String = "project",
    description: String = "description",
    createdBy: String = "admin",
    projectLogs: List<String> = emptyList(),
    projectState: String = "ACTIVE",
    taskStates: List<String> = listOf("TODO", "IN_PROGRESS", "DONE"),
    projectStates: List<String> = listOf("ACTIVE", "ARCHIVED")
): Project {
    return Project(
        id =  UUID.fromString(id),
        name = name,
        description = description,
        createdBy = createdBy,
        projectLogs = projectLogs,
        projectState = projectState,
        taskStates = taskStates,
        projectStates = projectStates
    )
}