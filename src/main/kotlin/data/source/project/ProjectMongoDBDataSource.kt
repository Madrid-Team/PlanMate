package org.madrid.data.source.project

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.project.ProjectDto
import data.source.project.ExternalProjectDataSource
import domain.utlis.ProjectExceptions
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class ProjectMongoDBDataSource(
    private val collection: MongoCollection<ProjectDto>
) : ExternalProjectDataSource {

    override suspend fun getProjects(): List<ProjectDto> {
        return collection.find().toList()
    }

    override suspend fun createProject(project: ProjectDto) {
        collection.insertOne(project)
    }

    override suspend fun deleteProject(projectId: String) {
        val query = eq("_id", projectId)
        collection.deleteOne(query)
    }

    override suspend fun editProject(project: ProjectDto) {

        val query = eq("_id", project.id)

        collection.replaceOne( query, project)
    }

    override suspend fun getProjectLogsById(id: String): List<String> {
        val filter = eq("_id", id)
        return collection.find(filter).toList().flatMap { it.projectLogs }
    }

    override suspend fun getProjectById(id: String): ProjectDto {
        val filter = eq("_id", id)
        return collection.find(filter).firstOrNull() ?: throw ProjectExceptions.ProjectNotFoundException()
    }

}