package data.source.csv.project

import data.utils.PlanMateColumnIndex.ProjectColumnsIndex.PROJECT_CREATED_BY
import data.utils.PlanMateColumnIndex.ProjectColumnsIndex.PROJECT_DESCRIPTION
import data.utils.PlanMateColumnIndex.ProjectColumnsIndex.PROJECT_ID
import data.utils.PlanMateColumnIndex.ProjectColumnsIndex.PROJECT_LOGS
import data.utils.PlanMateColumnIndex.ProjectColumnsIndex.PROJECT_NAME
import data.utils.PlanMateColumnIndex.ProjectColumnsIndex.PROJECT_STATES
import data.utils.PlanMateColumnIndex.ProjectColumnsIndex.PROJECT_TASKS_STATES
import data.dto.project.ProjectDto
import data.utils.PlanMateColumnIndex.ProjectColumnsIndex.PROJECT_STATE

class ProjectCsvParser() {


    fun parseOneRowToProject(row: String): ProjectDto {
        val result = row.split(",")
        return ProjectDto(
            id = result[PROJECT_ID],
            name = result[PROJECT_NAME],
            description = result[PROJECT_DESCRIPTION],
            createdBy = result[PROJECT_CREATED_BY],
            projectLogs = result[PROJECT_LOGS].split("|"),
            projectState = result[PROJECT_STATE],
            taskStates = result[PROJECT_TASKS_STATES].split("|"),
            projectStates = result[PROJECT_STATES].split("|"),
        )
    }

    //user israa creare p 123
    //user hdh update hhd from jhhf to hghgh " "

    fun parseProjectToString(project: ProjectDto): String {
        val projectCsvLine = listOf(
            project.id,
            project.name,
            project.description,
            project.createdBy,
            project.projectLogs.joinToString("|"),
            project.projectState,
            project.taskStates.joinToString("|"),
            project.projectStates.joinToString("|"),
            "\n"

        ).joinToString(",")

        return projectCsvLine
    }
}