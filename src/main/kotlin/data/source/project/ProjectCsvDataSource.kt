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
    override fun getProjects(): Result<List<ProjectDto>> {

        val projects = mutableListOf<ProjectDto>()

        return try {

            fileCsvReader.readCsvFile().forEach { row ->
                if (row.isNotEmpty()) {
                    projects.add(projectCsvParser.parseOneRowToProject(row))
                }
            }

            Result.success(projects)

        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override fun createProject(project: ProjectDto): Result<Unit> {
        return try {

            val row = projectCsvParser.parseProjectToString(project)
            fileCsvWriter.writeToCsvFile(row)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun deleteProject(projects: List<ProjectDto>): Result<Unit> {
        return try {
            var projectFileContentAfterDeletion = String.projectHeader
            projects.forEach {
                val projectAsString = projectCsvParser.parseProjectToString(it)
                projectFileContentAfterDeletion += projectAsString
            }
            fileCsvWriter.updateCsvFile(projectFileContentAfterDeletion)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun editProject(projects: List<ProjectDto>): Result<Unit> {
        return try {
            var projectAfterUpdate = String.projectHeader
            projects.forEach {
                val projectAsString = projectCsvParser.parseProjectToString(it)
                projectAfterUpdate += projectAsString
            }
            fileCsvWriter.updateCsvFile(projectAfterUpdate)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}