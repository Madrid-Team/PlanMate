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
        TODO("Not yet implemented")
    }

    override fun createProject(project: ProjectDto): Result<Unit> {
        TODO()
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

    override fun editProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }
}