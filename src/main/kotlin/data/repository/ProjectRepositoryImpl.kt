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

    override suspend fun deleteProjectByProjectId(projectId: String) = executeProjectOperation {
        projectExternalDataSource.deleteProject(projectId)
    }

    override suspend fun editProject(project: Project) = executeProjectOperation {
        projectExternalDataSource.editProject(project.toDto())
    }

    override suspend fun getProjectLogsByProjectId(projectId: String): List<String> = executeProjectOperation {
        projectExternalDataSource.getProjectLogsById(projectId) ?: throw ProjectExceptions.ProjectNotFoundException()
    }


    override suspend fun getProjectById(projectId: String): Project = executeProjectOperation {
        projectExternalDataSource.getProjectById(projectId)?.toDomain() ?: throw ProjectExceptions.ProjectNotFoundException()
    }

    private suspend fun <T> executeProjectOperation(operation: suspend () -> T): T {
        return try {
            operation()
        } catch (e: Exception) {
            throw e.toProjectException()
        }
    }


}