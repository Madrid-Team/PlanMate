package data.source.project

import data.dto.project.ProjectDto

interface ProjectDataSource {

    fun getProjects(): Result<List<ProjectDto>>
    fun createProject(project: ProjectDto):Result<Unit>
    fun deleteProject(projectId: String):Result<Unit>
    fun editProject(projects: List<ProjectDto>): Result<Unit>
}