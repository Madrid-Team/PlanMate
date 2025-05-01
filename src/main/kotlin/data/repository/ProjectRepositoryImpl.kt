package data.repository

import data.source.project.ProjectDataSource
import data.dto.project.ProjectDto
import domain.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
):ProjectRepository {
    override fun getAllProjects(): List<ProjectDto> {
        TODO("Not yet implemented")
    }

    override fun createProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun editProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }


}