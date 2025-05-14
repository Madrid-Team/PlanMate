package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.csv.project.ProjectExternalDataSource
import data.source.csv.user.CurrentUserProvider
import data.utils.toProjectException
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions

class ProjectRepositoryImpl(
    private val projectExternalDataSource: ProjectExternalDataSource,
    private val currentUserProvider: CurrentUserProvider
) : ProjectRepository {


    override suspend fun getAllProjects(): List<Project> = executeProjectOperation {
        projectExternalDataSource.getProjects(currentUserProvider.getCurrentUser()).map { it.toDomain() }
    }

    override suspend fun createProject(project: Project) = executeProjectOperation {
        projectExternalDataSource.createProject(project.toDto())
    }

    override suspend fun deleteProject(projectId: String) = executeProjectOperation {
        projectExternalDataSource.deleteProject(projectId)
    }

    override suspend fun editProject(project: Project) = executeProjectOperation {
        projectExternalDataSource.editProject(project.toDto())
    }

    override suspend fun getProjectLogsById(id: String): List<String> = executeProjectOperation {
        projectExternalDataSource.getProjectLogsById(id) ?: throw ProjectExceptions.ProjectNotFoundException()
    }


    override suspend fun getProjectById(id: String): Project = executeProjectOperation {
        projectExternalDataSource.getProjectById(id)?.toDomain() ?: throw ProjectExceptions.ProjectNotFoundException()
    }

    private suspend fun <T> executeProjectOperation(operation: suspend () -> T): T {
        return try {
            operation()
        } catch (e: Exception) {
            throw e.toProjectException()
        }
    }


}