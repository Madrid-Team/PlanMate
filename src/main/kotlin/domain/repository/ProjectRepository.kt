package domain.repository

import domain.models.project.Project

interface ProjectRepository {

    suspend fun getAllProjects(): List<Project>
    suspend fun createProject(project: Project)
    suspend fun deleteProject(projectId: String)
    fun editProject(project: Project)
    fun getProjectLogsById(id: String): List<String>
    fun getProjectById(id: String):Project
}