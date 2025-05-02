package domain.repository

import domain.models.project.Project

interface ProjectRepository {

    fun getAllProjects(): Result<List<Project>>
    fun createProject(project: Project): Result<Unit>
    fun deleteProject(projectId: String): Result<Unit>
    fun editProject(project: Project): Result<Unit>
    fun getProjectLogsById(id: String): Result<List<String>>
}