package data.source.project

import data.dto.project.ProjectDto

interface ProjectDataSource {

    fun createProject(project: ProjectDto):Result<Unit>
    fun deleteProject(projectId: String):Result<Unit>
    fun editProject(project: ProjectDto): Result<Unit>
}