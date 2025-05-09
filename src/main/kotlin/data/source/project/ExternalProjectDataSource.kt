package data.source.project

import data.dto.project.ProjectDto

interface ExternalProjectDataSource {
    suspend fun getProjects(): List<ProjectDto>
    suspend fun createProject(project: ProjectDto)
    suspend fun deleteProject(projectId: String)
    suspend fun editProject(project: ProjectDto)
    suspend fun getProjectLogsById(id: String): List<String>?
    suspend fun getProjectById(id: String): ProjectDto?
}