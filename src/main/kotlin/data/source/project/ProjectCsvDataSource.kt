package data.source.project

import data.dto.project.ProjectDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter

class ProjectCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val projectCsvParser: ProjectCsvParser
) : ProjectDataSource {
    override fun getProjects(): Result<List<ProjectDto>> {

        val projects = mutableListOf<ProjectDto>()

        return try {

            fileCsvReader.readCsvFile().forEach { row ->
                projects.add(projectCsvParser.parseOneRowToProject(row))
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
            var projectFileContentAfterDeletion = ""
            projects.forEach {
                val projectAsString = projectCsvParser.parseProjectToString(it) + "\n"
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
            var projectAfterUpdate = ""
            projects.forEach {
                val projectAsString = projectCsvParser.parseProjectToString(it) + "\n"
                projectAfterUpdate += projectAsString
            }
            fileCsvWriter.updateCsvFile(projectAfterUpdate)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}