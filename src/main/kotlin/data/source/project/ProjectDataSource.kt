package data.source.project

import data.dto.project.Project

interface ProjectDataSource {

    fun createProject(project: Project):Result<Unit>
    fun deleteProject(projectId: String):Result<Unit>
    fun editProject(project: Project): Result<Unit>
}