package data.repository

import data.dto.project.ProjectDto
import data.source.project.ProjectDataSource
import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {
    private lateinit var projects: MutableList<ProjectDto>

    init {
        initProjects()
    }

    private fun initProjects() {
        val result = projectDataSource.getProjects()
        projects = if (result.isSuccess) {
            result.getOrThrow().toMutableList()
        } else {
            mutableListOf()
        }
    }

    override fun getAllProjects(): List<ProjectDto> {
        return projects.ifEmpty {
            throw PlanMateExceptions("You haven't any projects yet")
        }
    }


    override fun createProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: String): Result<Unit> {

        val projectToRemove = projects.find { it.id == projectId }


        return if (projectToRemove != null) {
            try {
                projects.remove(projectToRemove)

                val result = projectDataSource.deleteProject(projects)

                if (result.isSuccess) {
                    Result.success(Unit)
                } else {
                    projects.add(projectToRemove)
                    Result.failure(result.exceptionOrNull() ?: PlanMateExceptions("Failed to delete project"))
                }
            } catch (e: Exception) {
                projects.add(projectToRemove)
                Result.failure(e)
            }
        } else {
            Result.failure(PlanMateExceptions("Project with ID $projectId not found"))
        }
    }

    override fun editProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }


}