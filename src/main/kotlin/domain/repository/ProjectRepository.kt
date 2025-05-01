package domain.repository

import data.dto.project.ProjectDto

interface ProjectRepository {

    fun getAllProjects(): List<ProjectDto>
    fun createProject(project: ProjectDto): Result<Unit>
    fun deleteProject(projectId: String): Result<Unit>
    fun editProject(project: ProjectDto): Result<Unit>
}