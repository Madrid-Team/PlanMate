package domain.mapper

import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import data.dto.project.ProjectDto
import data.dto.task.TaskDto
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.models.project.Project
import domain.models.task.Task

fun UserDto.toDomain() =
    User(
        id = id,
        username = username,
        passwordHash = passwordHash,
        role = role.toDomain(),
    )

fun UserRoleDto.toDomain() = when (this) {
    UserRoleDto.ADMIN -> UserRole.ADMIN
    UserRoleDto.MATE -> UserRole.MATE
}

fun ProjectDto.toDomain() =
    Project(
        id = id,
        name = name,
        description = description,
        createdBy = createdBy,
        projectLogs = projectLogs,
        projectState = projectState,
        taskStates = taskStates,
        projectStates = projectStates
    )

fun TaskDto.toDomain() =
    Task(
        id = id,
        projectId = projectId,
        description = description,
        createdBy = createdBy,
        logs = logs,
        state = state,
        title = title,
    )