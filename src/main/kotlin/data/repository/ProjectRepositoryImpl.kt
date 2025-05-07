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

    override fun getAllProjects(): List<Project> {
        val allProjects = projectMemoryDataSource.getProjects()
        return allProjects.ifEmpty {
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

    override fun deleteProject(projectId: String) {
        try {
            val projectListAfterDeleteProject = projectMemoryDataSource.deleteProject(projectId)
         projectDataSource.deleteProject(projectListAfterDeleteProject.map { it.toDto() })
        } catch (exception: Exception) {
            throw exception.toProjectException()
        }
    }

    override fun editProject(project: Project) {
        try {
            val projectListAfterUpdateProject = projectMemoryDataSource.editProject(project)
            projectDataSource.editProject(projectListAfterUpdateProject.map { it.toDto() })

        } catch (exception: Exception) {
            throw exception.toProjectException()
        }

    }

    override fun getProjectLogsById(id: String): List<String> {
        try {
            val project = projectMemoryDataSource.getProjects().find { it.id.toString() == id }
            return project?.projectLogs ?: throw ProjectExceptions.ProjectNotFoundException()
        } catch (exception: Exception) {
            throw exception.toProjectException()
        }
    }

    override fun getProjectById(id: String): Project {
        try {
            return projectMemoryDataSource.getProjects().find { it.id.toString() == id }
                ?: throw ProjectExceptions.ProjectNotFoundException()
        } catch (exception: Exception) {
            throw exception.toProjectException()
        }
    }

}