package data.repository

import data.dto.project.ProjectDto
import data.source.project.ProjectDataSource
import domain.mapper.toDomain
import domain.mapper.toDto
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectsFileNotExistsException
import domain.utlis.ProjectsReadWriteException
import java.io.FileNotFoundException
import java.io.IOException

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    private var projects: MutableList<Project> = mutableListOf()

    init {
        initProjects()
    }

    private fun initProjects() {
        val result = projectDataSource.getProjects()
        projects = if (result.isSuccess) {
            val dtoList = result.getOrNull() as? List<ProjectDto> ?: emptyList()
            dtoList.map { it.toDomain() }.toMutableList()
        } else {
            mutableListOf()
        }
    }

    override fun getAllProjects(): List<Project> {
        return projects
    }


    override fun createProject(project: Project): Result<Unit> {

        val result = projectDataSource.createProject(project.toDto())

        return if (result.isSuccess) {
            projects.add(project)
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

        val projectToRemove = projects.find { it.id == projectId }


        return if (projectToRemove != null) {
            projects.remove(projectToRemove)

            val result = projectDataSource.deleteProject(projects.map { it.toDto() })

            if (result.isSuccess) {
                Result.success(Unit)
            } else {
                projects.add(projectToRemove)
                Result.failure(result.exceptionOrNull() ?: PlanMateExceptions("Failed to delete project"))
            }

        } else {
            Result.failure(PlanMateExceptions("Project with ID $projectId not found"))
        }
    }

    override fun editProject(project: Project): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getProjectLogsById(id: String): Result<List<String>> {
        TODO("Not yet implemented")
    }

}