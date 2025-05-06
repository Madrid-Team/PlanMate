package data.source.project

import data.dto.project.ProjectDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.projectHeader

class ProjectCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val projectCsvParser: ProjectCsvParser
) : ProjectDataSource {
    override fun getProjects(): List<ProjectDto> {

        val projects = mutableListOf<ProjectDto>()

        fileCsvReader.readCsvFile().forEach { row ->
            if (row.isNotEmpty()) {
                projects.add(projectCsvParser.parseOneRowToProject(row))
            }
        }

        return projects


    }

    override fun createProject(project: ProjectDto) {
            val row = projectCsvParser.parseProjectToString(project)
            fileCsvWriter.writeToCsvFile(row)
    }

    override fun deleteProject(projects: List<ProjectDto>) {

            var projectFileContentAfterDeletion = String.projectHeader
            projects.forEach {
                val projectAsString = projectCsvParser.parseProjectToString(it)
                projectFileContentAfterDeletion += projectAsString
            }
            fileCsvWriter.updateCsvFile(projectFileContentAfterDeletion)

    }

    override fun editProject(projects: List<ProjectDto>){

            var projectAfterUpdate = String.projectHeader
            projects.forEach {
                val projectAsString = projectCsvParser.parseProjectToString(it)
                projectAfterUpdate += projectAsString
            }
            fileCsvWriter.updateCsvFile(projectAfterUpdate)
    }
}