package data.repository

import data.source.ProjectDataSource
import domain.models.project.Project
import domain.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
):ProjectRepository {

    override fun createProject(project: Project) {
        TODO()
    }

    override fun deleteProject(projectId: String) {
        TODO()
    }

    override fun editProject(project: Project) {
        TODO("Not yet implemented")
    }

}