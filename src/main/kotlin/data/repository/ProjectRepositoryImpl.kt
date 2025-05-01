package data.repository

import data.dto.project.ProjectDto
import data.source.project.ProjectDataSource
import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectsFileNotExistsException
import domain.utlis.ProjectsReadWriteException
import java.io.FileNotFoundException
import java.io.IOException

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

        val result = projectDataSource.createProject(project)

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
        TODO()
    }

    override fun editProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }

}