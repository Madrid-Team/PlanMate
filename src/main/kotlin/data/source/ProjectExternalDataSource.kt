package data.source

import data.dto.authentication.UserDto
import data.dto.project.ProjectDto

interface ProjectExternalDataSource {
    suspend fun getProjects(user: UserDto): List<ProjectDto>
    suspend fun createProject(project: ProjectDto)
    suspend fun deleteProject(projectId: String)
    suspend fun editProject(project: ProjectDto)
    suspend fun getProjectLogsById(id: String): List<String>?
    suspend fun getProjectById(id: String): ProjectDto?
}