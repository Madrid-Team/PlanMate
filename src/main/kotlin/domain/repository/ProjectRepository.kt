package domain.repository

import data.dto.project.Project

interface ProjectRepository {

    fun getAllProjects(): List<Project>
    fun createProject(project: Project)
    fun deleteProject(projectId: String)
    fun editProject(project: Project)
}