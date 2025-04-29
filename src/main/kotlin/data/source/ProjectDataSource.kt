package data.source

import domain.models.project.Project

interface ProjectDataSource {

    fun createProject(project: Project):Result<Unit>
    fun deleteProject(projectId: String):Result<Unit>
}