package data.source.project

import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.dto.project.Project

class ProjectCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val projectCsvParser: ProjectCsvParser
) : ProjectDataSource {

    override fun createProject(project: Project): Result<Unit> {
        val projectRow = projectCsvParser.parseProjectToString(project)
        fileCsvWriter.writeProjectToCsvFile(projectRow)
        return Result.success(Unit)
    }

    override fun deleteProject(projectId: String): Result<Unit> {
        TODO()
    }

    override fun editProject(project: Project): Result<Unit> {
        TODO("Not yet implemented")
    }
}