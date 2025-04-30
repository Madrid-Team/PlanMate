package data.source.project

import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.dto.project.ProjectDto

class ProjectCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val projectCsvParser: ProjectCsvParser
) : ProjectDataSource {

    override fun createProject(project: ProjectDto): Result<Unit> {
        TODO()
    }

    override fun deleteProject(projectId: String): Result<Unit> {
        TODO()
    }

    override fun editProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }
}