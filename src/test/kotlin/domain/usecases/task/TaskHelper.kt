package domain.usecases.task

import data.dto.task.TaskDto
import domain.models.task.Task
import java.util.*

fun createTask(
    id: String = UUID.randomUUID().toString(),
    projectId: String = "",
    title: String = "",
    description: String = "",
    state: String = "",
    createdBy: String = "",
    logs: List<String> = listOf(),
) = Task(
    id =  UUID.fromString(id),
    projectId = projectId,
    title = title,
    description = description,
    taskState = state,
    createdBy = createdBy,
    logs = logs
)