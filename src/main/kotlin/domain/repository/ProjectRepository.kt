package domain.repository

import domain.models.project.Project

interface ProjectRepository {

    suspend fun getAllProjects(): List<Project>
    suspend fun createProject(project: Project)
    suspend fun deleteProject(projectId: String)
    suspend fun editProject(project: Project)
    suspend fun getProjectLogsById(id: String): List<String>
    suspend fun getProjectById(id: String):Project
}