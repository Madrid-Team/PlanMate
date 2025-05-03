package data.repository

import data.dto.project.ProjectDto
import data.mapper.toDomain
import data.mapper.toDto
import data.source.project.ProjectDataSource
import data.source.project.ProjectMemoryDataSource
import domain.models.logs.CreatedLogFormatter
import domain.models.logs.EntityType
import domain.models.logs.UpdatedLogFormatter
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.*
import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDateTime

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val projectMemoryDataSource: ProjectMemoryDataSource,
) : ProjectRepository {


    init {
        initProjects()
    }

    private fun initProjects() {
        val result = projectDataSource.getProjects()
        val projects = if (result.isSuccess) {
            val dtoList = result.getOrNull() as? List<ProjectDto> ?: emptyList()
            dtoList.map { it.toDomain() }.toMutableList()
        } else {
            mutableListOf()
        }
        projectMemoryDataSource.setProjects(projects)
    }

    override fun getAllProjects(): Result<List<Project>> {
        val allProjects = projectMemoryDataSource.getProjects()
        return if (allProjects.isNotEmpty()) {
            Result.success(allProjects)
        } else {
            Result.failure(PlanMateExceptions("You haven't any projects yet"))
        }
    }


    override fun createProject(project: Project): Result<Unit> {

        val result = projectDataSource.createProject(project.toDto())

        return if (result.isSuccess) {
            projectMemoryDataSource.addProject(project)
            result
        } else {
            when (val exception = result.exceptionOrNull()) {
                is FileNotFoundException -> Result.failure(ProjectsFileNotExistsException())
                is IOException -> Result.failure(ProjectsReadWriteException())
                else -> {
                    Result.failure(PlanMateExceptions(exception?.message.toString()))
                }
            }
        }
    }

    override fun deleteProject(projectId: String): Result<Unit> {


        val projects = projectMemoryDataSource.getProjects()
        val currentProject = projects.find { it.id.toString() == projectId }
        val projectListAfterDeleteProject = projectMemoryDataSource.deleteProject(projectId)

       val result = projectDataSource.deleteProject(projectListAfterDeleteProject.map { it.toDto() })

           return if (result.isSuccess) {
                Result.success(Unit)
            } else {
               projectMemoryDataSource.addProject(currentProject!!)
                Result.failure(result.exceptionOrNull() ?: PlanMateExceptions("Failed to delete project"))
            }

    }

    override fun editProject(project: Project): Result<Unit> {

        val projectListAfterUpdateProject = projectMemoryDataSource.editProject(project)

            val result = projectDataSource.editProject(projectListAfterUpdateProject.map { it.toDto() })

            return if (result.isSuccess) {
                Result.success(Unit)
            } else {
                projectMemoryDataSource.addProject(project)
                Result.failure(result.exceptionOrNull() ?: PlanMateExceptions("Failed to edit project"))
            }

    }

    override fun getProjectLogsById(id: String): Result<List<String>> {

        val project = projectMemoryDataSource.getProjects().find { it.id.toString() == id }
        return if (project != null) {
            Result.success(project.projectLogs)
        } else {
            Result.failure(ProjectNotFoundException())
        }
    }

    override fun getProjectById(id: String): Project? {
        return projectMemoryDataSource.getProjects().find { it.id.toString() == id }
    }

}