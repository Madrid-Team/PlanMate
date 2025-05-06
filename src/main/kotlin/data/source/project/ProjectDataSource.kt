package data.source.project

import data.dto.project.ProjectDto

interface ProjectDataSource {

    fun getProjects(): List<ProjectDto>
    fun createProject(project: ProjectDto)
    fun deleteProject(projects: List<ProjectDto>)
    fun editProject(projects: List<ProjectDto>)
}