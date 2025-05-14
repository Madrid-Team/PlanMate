package domain.repository

import domain.models.project.Project

interface ProjectRepository {

    suspend fun getAllProjects(): List<Project>
    suspend fun createProject(project: Project)
    suspend fun deleteProject(projectId: String)
    suspend fun editProject(project: Project)
    suspend fun getProjectLogsByProjectId(projectId: String): List<String>
    suspend fun getProjectById(projectId: String):Project
}