package data.mapper

import data.dto.authentication.UserDto
import data.dto.project.ProjectDto
import data.dto.task.TaskDto
import domain.models.authentication.User
import domain.models.project.Project
import domain.models.task.Task
import java.util.*

fun UserDto.toDomain() =
    User(
        id = UUID.fromString(id),
        username = username,
        passwordHash = passwordHash,
        role = role,
    )

fun User.toDto() =
    UserDto(
        id = id.toString(),
        username = username,
        passwordHash = passwordHash,
        role = role,
    )


fun ProjectDto.toDomain() =
    Project(
        id = UUID.fromString(id),
        name = name,
        description = description,
        createdBy = createdBy,
        projectLogs = projectLogs,
        projectState = projectState,
        taskStates = taskStates,
        projectStates = projectStates,
    )

fun Project.toDto() =
    ProjectDto(
        id = id.toString(),
        name = name,
        description = description,
        createdBy = createdBy,
        projectLogs = projectLogs,
        projectState = projectState,
        taskStates = taskStates,
        projectStates = projectStates,
    )


fun TaskDto.toDomain() =
    Task(
        id =  UUID.fromString(id),
        projectId = projectId,
        description = description,
        createdBy = createdBy,
        logs = logs,
        state = state,
        title = title,
    )


fun Task.toDto() =
    TaskDto(
        id = id.toString(),
        projectId = projectId,
        description = description,
        createdBy = createdBy,
        logs = logs,
        state = state,
        title = title,
    )