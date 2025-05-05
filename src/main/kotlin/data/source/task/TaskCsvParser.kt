package data.source.task

import data.dto.task.TaskDto
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.PROJECT_ID
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_CREATED_BY
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_DESCRIPTION
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_ID
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_LOGS
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_STATES
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_TITLE
import domain.models.task.Task

class TaskCsvParser() {
    fun parseOneRowToTask(row: String): TaskDto {
        val result = row.split(",")
        return TaskDto(
            id = result[TASK_ID],
            projectId = result[PROJECT_ID],
            title = result[TASK_TITLE],
            description = result[TASK_DESCRIPTION],
            taskState = result[TASK_STATES],
            createdBy = result[TASK_CREATED_BY],
            logs = result[TASK_LOGS].split("|")
        )
    }

    fun parseTaskToString(task: TaskDto): String {
        val taskCsvLine = listOf(
            task.id,
            task.projectId,
            task.description,
            task.createdBy,
            task.taskState,
            task.title,
            task.logs.joinToString("|")
        ).joinToString(",")

        return taskCsvLine
    }
}