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

    override fun createProject(project: ProjectDto) {
        TODO()
    }

    override fun deleteProject(projectId: String) {
        TODO()
    }

    override fun editProject(project: ProjectDto) {
        TODO("Not yet implemented")
    }

}