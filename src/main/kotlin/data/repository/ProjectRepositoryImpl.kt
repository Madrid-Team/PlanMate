package data.repository

import data.source.project.ProjectDataSource
import data.dto.project.Project
import domain.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
):ProjectRepository {
    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun createProject(project: Project) {
        TODO()
    }

    override fun deleteProject(projectId: String) {
        TODO()
    }

    override fun editProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun getProjectById(id: String): Project? {
        TODO("Not yet implemented")
    }

}