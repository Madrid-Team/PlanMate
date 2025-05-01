package domain.repository

import data.dto.project.Project

interface ProjectRepository {

    fun getAllProjects(): List<Project>
    fun createProject(project: Project): Result<Unit>
    fun deleteProject(projectId: String): Result<Unit>
    fun editProject(project: Project): Result<Unit>
    fun getProjectLogsById(id: String): Result<List<String>>
    fun getProjectById(id: String):Project?
}