package domain.repository

import data.dto.project.ProjectDto

interface ProjectRepository {

    fun createProject(project: ProjectDto)
    fun deleteProject(projectId: String)
    fun editProject(project: ProjectDto)
}