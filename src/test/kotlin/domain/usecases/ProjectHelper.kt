package domain.usecases

import domain.models.project.Project
import java.util.*

fun createProject(
    name: String = "",
    description: String = "",
    id: String = UUID.randomUUID().toString(),
    createdBy:String = "",
    projectState:String = "",
    taskStates: List<String> = listOf(),
    projectStates: List<String> = listOf(),
    ) = Project(
        id = id,
        name = name,
        description = description,
        createdBy = createdBy,
        projectLogs = emptyList(),
        projectState =projectState,
        taskStates = taskStates,
        projectStates = projectStates,
    )