package domain.repository

import domain.models.project.Project

interface ProjectRepository {

    fun createProject(project: Project)
    fun deleteProject(projectId: String)
    fun editProject(project: Project)
}