package data

import data.dto.project.ProjectDto
import domain.models.project.Project
import java.util.*


fun createProject(
    name: String = "",
    description: String = "",
    id: String = UUID.randomUUID().toString(),
    createdBy:String = "",
    projectState:String = "",
    taskStates: List<String> = listOf(),
    projectLogs:List<String> = listOf(),
    projectStates: List<String> = emptyList(),
) = ProjectDto(
    id = id,
    name = name,
    description = description,
    createdBy = createdBy,
    projectLogs = projectLogs,
    projectState =projectState,
    taskStates = taskStates,
    projectStates = projectStates,
)