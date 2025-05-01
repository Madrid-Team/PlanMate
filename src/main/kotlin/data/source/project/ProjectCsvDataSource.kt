package data.source.project

import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.dto.project.ProjectDto

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

    override fun deleteProject(projectId: String): Result<Unit> {
        TODO()
    }

    override fun editProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }
}