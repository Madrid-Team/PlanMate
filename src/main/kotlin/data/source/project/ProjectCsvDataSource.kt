package data.source.project

import data.dto.project.ProjectDto
import data.mapper.toDomain
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.projectHeader
import data.utils.toProjectException
import domain.models.project.Project
import domain.utlis.ProjectExceptions

class ProjectCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val projectCsvParser: ProjectCsvParser,
    private val projectManager: ProjectManager,
) : ExternalProjectDataSource {

    init {
        readProjectManagerListFromFile()
    }

    private fun readProjectManagerListFromFile() {
        if (projectManager.getProjects().isEmpty()) {
            val projects = mutableListOf<ProjectDto>()

            fileCsvReader.readCsvFile().forEach { row ->
                if (row.isNotEmpty()) {
                    projects.add(projectCsvParser.parseOneRowToProject(row))
                }
            }
            projectManager.setProjects(projects)
        }
    }

    override suspend fun getProjects(): List<ProjectDto> {
        return projectManager.getProjects()
    }

    override suspend fun createProject(project: ProjectDto) {
        try {
            val row = projectCsvParser.parseProjectToString(project)
            fileCsvWriter.writeToCsvFile(row)
            projectManager.addProject(project)
        } catch (e: Exception) {
            throw e.toProjectException()
        }
    }

    override suspend fun deleteProject(projectId: String) {
        val projectWillDeleted = projectManager.getProjects().find { project -> project.id == projectId }

        try {
            var projectListAfterDeletion = projectManager.deleteProject(projectId)
            var stringAfterDelete = String.projectHeader
            projectListAfterDeletion.forEach {
                val projectAsString = projectCsvParser.parseProjectToString(it)
                stringAfterDelete += projectAsString
            }
            fileCsvWriter.updateCsvFile(stringAfterDelete)
        } catch (e: Exception) {
            projectWillDeleted?.let { projectManager.addProject(projectWillDeleted) }
            throw e.toProjectException()
        }

    }

    override suspend fun editProject(project: ProjectDto) {

        try {
            var projectListAfterEdition = projectManager.editProject(project)

            var projectAfterUpdate = String.projectHeader
            projectListAfterEdition.forEach {
                val projectAsString = projectCsvParser.parseProjectToString(it)
                projectAfterUpdate += projectAsString
            }
            fileCsvWriter.updateCsvFile(projectAfterUpdate)
        } catch (e: Exception) {
            projectManager.addProject(project)
            throw e.toProjectException()
        }
    }

    override suspend fun getProjectLogsById(id: String): List<String> {
        return try {
            projectManager.getProjects().find { it.id == id }?.projectLogs
                ?: throw ProjectExceptions.ProjectNotFoundException()
        } catch (e: Exception) {
            throw e.toProjectException()
        }
    }

    override suspend fun getProjectById(id: String): Project {
        return try {
            projectManager.getProjects().find { it.id == id }?.toDomain() ?: throw ProjectExceptions.ProjectNotFoundException()
        } catch (e: Exception) {
            throw e.toProjectException()
        }
    }
}