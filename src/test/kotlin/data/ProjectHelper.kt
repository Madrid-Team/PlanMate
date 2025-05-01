package data

import data.dto.project.ProjectDto
import java.util.*

fun createProject(
    name: String = "",
    description: String = "",
    id: String = UUID.randomUUID().toString(),
    createdBy:String = "",
    projectState:String = "",
    taskStates: List<String> = listOf(),
) = ProjectDto(
    id = id,
    name = name,
    description = description,
    createdBy = createdBy,
    projectLogs = emptyList(),
    projectState =projectState,
    taskStates = taskStates
)