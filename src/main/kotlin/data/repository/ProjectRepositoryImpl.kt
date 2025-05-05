package data.repository

import data.dto.project.ProjectDto
import data.mapper.toDomain
import data.mapper.toDto
import data.source.project.ProjectDataSource
import data.source.project.ProjectMemoryDataSource
import data.utils.toProjectException
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectExceptions

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val projectMemoryDataSource: ProjectMemoryDataSource,
) : ProjectRepository {


    init {
        initProjects()
    }

    private fun initProjects() {
        val result = projectDataSource.getProjects()
        if (result.isSuccess) {
            val dtoList = result.getOrNull() as? List<ProjectDto> ?: emptyList()
            val a = dtoList.map { it.toDomain() }.toMutableList()
            projectMemoryDataSource.setProjects(a)
        } else {
            mutableListOf<Project>()
        }

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
            return Result.failure(
                result.exceptionOrNull().toProjectException()
            )
        }
    }

    override fun deleteProject(projectId: String): Result<Unit> {

        val projectListAfterDeleteProject = projectMemoryDataSource.deleteProject(projectId)

        val result = projectDataSource.deleteProject(projectListAfterDeleteProject.map { it.toDto() })

        return if (result.isSuccess) {
            Result.success(Unit)
        } else {
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
            Result.failure(ProjectExceptions.ProjectNotFoundException())
        }
    }

    override fun getProjectById(id: String): Project? {
        return projectMemoryDataSource.getProjects().find { it.id.toString() == id }
    }

}