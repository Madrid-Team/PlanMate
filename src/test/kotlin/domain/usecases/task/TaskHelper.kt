package domain.usecases.task

import data.dto.task.TaskDto
import domain.models.task.Task
import java.util.*

fun createTask(
    id: String = "",
    projectId: String = "",
    title: String = "",
    description: String = "",
    state: String = "",
    createdBy: String = "",
    logs: List<String> = listOf(),
) = TaskDto(
    id =  id,
    projectId = projectId,
    title = title,
    description = description,
    taskState = state,
    createdBy = createdBy,
    logs = logs
)