package data.source.project

import data.dto.project.ProjectDto

interface ProjectDataSource {

    fun getProjects(): Result<List<ProjectDto>>
    fun createProject(project: ProjectDto):Result<Unit>
    fun deleteProject(projects: List<ProjectDto>):Result<Unit>
    fun editProject(project: ProjectDto): Result<Unit>
}