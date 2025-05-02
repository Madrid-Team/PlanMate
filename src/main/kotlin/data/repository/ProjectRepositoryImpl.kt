package data.repository

import data.dto.project.ProjectDto
import data.source.project.ProjectDataSource
import domain.mapper.toDomain
import domain.mapper.toDto
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

    override fun getAllProjects(): Result<List<Project>> {
        return if (projects.isNotEmpty()) {
            Result.success(projects)
        } else {
            Result.failure(PlanMateExceptions("You haven't any projects yet"))
        }
    }


    override fun createProject(project: Project): Result<Unit> {

        val result = projectDataSource.createProject(project.toDto())

        return if (result.isSuccess) {

            val log = CreatedLogFormatter.format(
                entityName = project.name,
                entityType = EntityType.PROJECT,
                username = project.createdBy,
            )
            projects.add(project.copy(projectLogs = listOf(log)))
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
        val oldProject = projects.find { it.id == project.id }

        return if (oldProject != null) {
            val indexOfUpdatedProject = projects.indexOf(oldProject)
            val logsForUpdates = createLogsForUpdatedFields(oldProject = oldProject , updatedProject = project)
            projects.add(index = indexOfUpdatedProject, project.copy(projectLogs = project.projectLogs + logsForUpdates))

            val result = projectDataSource.editProject(projects.map { it.toDto() })

            if (result.isSuccess) {
                Result.success(Unit)
            } else {
                projects[indexOfUpdatedProject] = oldProject
                Result.failure(result.exceptionOrNull() ?: PlanMateExceptions("Failed to edit project"))
            }
        } else {
            Result.failure(PlanMateExceptions("Project with ID $project.id not found"))
        }
    }

    override fun getProjectLogsById(id: String): Result<List<String>> {
        val project = projects.find { it.id == id }
        return if (project != null) {
            Result.success(project.projectLogs)
        } else {
            Result.failure(ProjectNotFoundException())
        }
    }

    override fun getProjectById(id: String): Project? {
        TODO("Not yet implemented")
    }


    private fun createLogsForUpdatedFields(oldProject: Project, updatedProject: Project): List<String> {
        val logs = mutableListOf<String>()
        val timestamp = LocalDateTime.now().convertDateIntoReadableDate()
        if (oldProject.name != updatedProject.name) {
            logs.add(
                //create log message contains the update on project name
                UpdatedLogFormatter.format(
                    entityName = oldProject.name,
                    entityType = EntityType.PROJECT,
                    username = oldProject.createdBy,
                    fieldName = "name",
                    oldValue = oldProject.name,
                    newValue = updatedProject.name,
                    timestamp = timestamp
                )
            )
        }

        if (oldProject.projectState != updatedProject.projectState) {
            logs.add(
                //create log message contains the update on project name
                UpdatedLogFormatter.format(
                    entityName = oldProject.name,
                    entityType = EntityType.PROJECT,
                    username = oldProject.createdBy,
                    fieldName = "state",
                    oldValue = oldProject.projectState,
                    newValue = updatedProject.projectState,
                    timestamp = timestamp
                )
            )
        }

        if (oldProject.description != updatedProject.description) {
            logs.add(
                //create log message contains the update on project name
                UpdatedLogFormatter.format(
                    entityName = oldProject.name,
                    entityType = EntityType.PROJECT,
                    username = oldProject.createdBy,
                    fieldName = "description",
                    oldValue = oldProject.description,
                    newValue = updatedProject.description,
                    timestamp = timestamp
                )
            )
        }

        return logs
    }
}