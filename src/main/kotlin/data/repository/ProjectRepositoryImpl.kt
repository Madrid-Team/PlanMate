package data.utils

import data.mapper.toDomain
import data.mapper.toDto
import data.source.project.ExternalProjectDataSource
import domain.models.project.Project
import domain.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val externalProjectDataSource: ExternalProjectDataSource,
) : ProjectRepository {


    override suspend fun getAllProjects(): List<Project> {
       return externalProjectDataSource.getProjects().map { it.toDomain() }
    }


    override suspend fun createProject(project: Project) {
       externalProjectDataSource.createProject(project.toDto())
    }

    override suspend fun deleteProject(projectId: String) {
       return externalProjectDataSource.deleteProject(projectId)
    }

    override suspend fun editProject(project: Project) {
       return externalProjectDataSource.editProject(project.toDto())

    }

    override suspend fun getProjectLogsById(id: String): List<String> {
      return externalProjectDataSource.getProjectLogsById(id)
    }

    override suspend fun getProjectById(id: String): Project {
       return externalProjectDataSource.getProjectById(id).toDomain()
    }

}