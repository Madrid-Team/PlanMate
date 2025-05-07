package data.utils

import data.mapper.toDomain
import data.mapper.toDto
import data.source.project.ProjectDataSource
import data.source.project.ProjectMemoryDataSource
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectExceptions
import org.madrid.data.source.project.RemoteProjectDataSource

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val remoteProjectDataSource: RemoteProjectDataSource,
    private val projectMemoryDataSource: ProjectMemoryDataSource,
) : ProjectRepository {


    init {
        initProjects()
    }

    private fun initProjects() {
        try {
            val result = projectDataSource.getProjects()
            val a = result.map { it.toDomain() }.toMutableList()
            projectMemoryDataSource.setProjects(a)
        } catch (_: Exception) {

        }

    }

    override suspend fun getAllProjects(): List<Project> {
        val allProjects = remoteProjectDataSource.getProjects()
        return allProjects.map { it.toDomain() }.ifEmpty {
            throw PlanMateExceptions("You haven't any projects yet")
        }
    }


    override suspend fun createProject(project: Project) {
        try {
            remoteProjectDataSource.createProject(project.toDto())
            projectMemoryDataSource.addProject(project)

        } catch (exception: Exception) {
            throw exception.toProjectException()
        }
    }

    override suspend fun deleteProject(projectId: String) {
        try {
            val projectListAfterDeleteProject = projectMemoryDataSource.deleteProject(projectId)
         remoteProjectDataSource.deleteProject(projectId)
        } catch (exception: Exception) {
            throw exception.toProjectException()
        }
    }

    override suspend fun editProject(project: Project) {
        try {
            val projectListAfterUpdateProject = projectMemoryDataSource.editProject(project)
            remoteProjectDataSource.editProject(project.toDto())

        } catch (exception: Exception) {
            throw exception.toProjectException()
        }

    }

    override suspend fun getProjectLogsById(id: String): List<String> {
        try {
            val project = remoteProjectDataSource.getProjects().find { it.id == id }
            return project?.projectLogs ?: throw ProjectExceptions.ProjectNotFoundException()
        } catch (exception: Exception) {
            throw exception.toProjectException()
        }
    }

    override suspend fun getProjectById(id: String): Project {
        try {
            return remoteProjectDataSource.getProjects().find { it.id.toString() == id }?.toDomain()
                ?: throw ProjectExceptions.ProjectNotFoundException()
        } catch (exception: Exception) {
            throw exception.toProjectException()
        }
    }

}