package org.madrid.data.source.project

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.project.ProjectDto
import data.source.project.ProjectDataSource
import kotlinx.coroutines.flow.toList
import org.madrid.data.utils.PROJECT_COLLECTION

class ProjectMongoDBDataSource(
    database: MongoDatabase
) : RemoteProjectDataSource {
    private val collection = database.getCollection<ProjectDto>(PROJECT_COLLECTION)

    override suspend fun getProjects(): List<ProjectDto> {
        return collection.find().toList()
    }

    override suspend fun createProject(project: ProjectDto) {
        collection.insertOne(project)
    }

    override suspend fun deleteProject(projectId: String) {
        val query = Filters.eq("_id", projectId)
        collection.deleteOne(query)
    }

    override suspend fun editProject(project: ProjectDto) {
        val query = Filters.eq("_id", project.id)
        val updateSet = Updates.set("_id", project.id)

        collection.updateOne(filter = query, update = updateSet)
    }

}