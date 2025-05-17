package data.mapper

import data.dto.AuditLogDto
import data.dto.authentication.UserDto
import data.dto.project.ProjectDto
import data.dto.task.TaskDto
import domain.models.authentication.User
import domain.models.logs.AuditLog
import domain.models.logs.EntityType.Companion.toEntityType
import domain.models.logs.OperationType.Companion.toOperationType
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
        id = UUID.fromString(id),
        projectId = projectId,
        description = description,
        createdBy = createdBy,
        taskLogs = taskLogs,
        taskState = taskState,
        title = title,
    )


fun Task.toDto() =
    TaskDto(
        id = id.toString(),
        projectId = projectId,
        description = description,
        createdBy = createdBy,
        taskLogs = taskLogs,
        taskState = taskState,
        title = title,
    )

fun AuditLogDto.toDomain() =
    AuditLog(
        id = UUID.fromString(id),
        operationType = toOperationType(operationType),
        entityName = entityName,
        entityType = toEntityType(entityType),
        entityId = entityId,
        projectId = projectId,
        username = username,
        fieldName = fieldName,
        oldValue = oldValue,
        newValue = newValue,
        timestamp = timestamp,
    )

fun AuditLog.toDto() =
    AuditLogDto(
        id = id.toString(),
        operationType = operationType.name,
        entityName = entityName,
        entityType = entityType.name,
        entityId = entityId,
        projectId = projectId,
        username = username,
        fieldName = fieldName,
        oldValue = oldValue,
        newValue = newValue,
        timestamp = timestamp,
    )

