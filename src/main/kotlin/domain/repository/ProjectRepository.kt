package domain.repository

import data.dto.project.Project

interface ProjectRepository {

    fun createProject(project: Project)
    fun deleteProject(projectId: String)
    fun editProject(project: Project)
}