package data.source.task

import data.dto.task.TaskDto
import java.util.*

fun helperTaskDto(
    id: String = UUID.randomUUID().toString(),
    projectId: String = "",
    title: String = "",
    description: String = "",
    state: String = "",
    createdBy: String = "",
    logs: List<String> = listOf(),
) = TaskDto(
    id = id,
    projectId = projectId,
    title = title,
    description = description,
    taskState = state,
    createdBy = createdBy,
    logs = logs
)