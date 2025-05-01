package data.repository

import data.source.project.ProjectDataSource
import data.dto.project.ProjectDto
import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectNotFoundException

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
):ProjectRepository {

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
        TODO("Not yet implemented")
    }

    override fun editProject(project: ProjectDto): Result<Unit> {
        val updatedProject = projects.find { it.id == project.id }
        val indexOfUpdatedProject = projects.indexOf(updatedProject)

        return if (updatedProject != null) {
            try {
                projects.add(index = indexOfUpdatedProject, updatedProject )

                val result = projectDataSource.editProject(projects)

                if (result.isSuccess) {
                    Result.success(Unit)
                } else {
                    Result.failure(result.exceptionOrNull() ?: Exception("Failed to delete project"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(PlanMateExceptions("Project with ID $updatedProject.id not found"))
        }
    }

}