package data.source

import data.utils.PlanMateColumnIndex.TaskColumnsIndex.PROJECT_ID
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_CREATED_BY
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_DESCRIPTION
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_ID
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_LOGS
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_STATES
import data.utils.PlanMateColumnIndex.TaskColumnsIndex.TASK_TITLE
import domain.models.task.Task

class TaskCsvParser() {
    fun parseOneRowToTask(row: String): Task {
        val result = row.split(",")
        return Task(
            id = result[TASK_ID],
            projectId = result[PROJECT_ID],
            title = result[TASK_TITLE],
            description = result[TASK_DESCRIPTION],
            state = result[TASK_STATES],
            createdBy = result[TASK_CREATED_BY],
            logs = result[TASK_LOGS].split("|")
        )
    }

    fun parseTaskToString(task: Task): String {
        val taskCsvLine = listOf(
            task.id,
            task.projectId,
            task.description,
            task.createdBy,
            task.state,
            task.title,
            task.logs.joinToString("|")
        ).joinToString(",")

        return taskCsvLine
    }
}