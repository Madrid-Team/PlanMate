package domain.repository

import domain.models.project.Project

interface ProjectRepository {

    fun getAllProjects(): List<Project>
    fun createProject(project: Project)
    fun deleteProject(projectId: String)
    fun editProject(project: Project)
    fun getProjectLogsById(id: String): List<String>
    fun getProjectById(id: String):Project
}