package org.madrid.data.source.project

import data.dto.project.ProjectDto
import data.source.project.ProjectDataSource
import org.madrid.data.source.mongoDb.MongoClientProvider

class ProjectMongoDBDataSource(
    private val mongoClientProvider: MongoClientProvider
): ProjectDataSource {
    override fun getProjects(): List<ProjectDto> {
        TODO("Not yet implemented")
    }

    override fun createProject(project: ProjectDto) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projects: List<ProjectDto>) {
        TODO("Not yet implemented")
    }

    override fun editProject(projects: List<ProjectDto>) {
        TODO("Not yet implemented")
    }

}