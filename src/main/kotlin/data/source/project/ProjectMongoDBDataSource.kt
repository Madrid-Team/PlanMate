package org.madrid.data.source.project

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.project.ProjectDto
import data.source.project.ProjectDataSource

class ProjectMongoDBDataSource(
    private val database: MongoDatabase
): ProjectDataSource {

    override fun getProjects(): List<ProjectDto> {
        TODO("Not yet implemented")
    }

    override fun createProject(project: ProjectDto) {

    }

    override fun deleteProject(projects: List<ProjectDto>) {

    }

    override fun editProject(projects: List<ProjectDto>) {
        TODO("Not yet implemented")
    }

}