package org.madrid.data.source.project

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.project.ProjectDto
import data.source.project.ProjectDataSource
import data.source.remote.MongoClientProvider
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.madrid.data.utils.OperationFailedException

class ProjectMongoDBDataSource(
    private val mongoClientProvider: MongoClientProvider,
    private val collection: MongoCollection<ProjectDto>
): ProjectDataSource {
    override fun getProjects(): Result<List<ProjectDto>> {
        TODO("Not yet implemented")
    }

    override fun createProject(project: ProjectDto): Result<Unit> {
        TODO("Not yet implemented")
    }

    suspend fun addProject(): ProjectDto {
        val projectItem = ProjectDto(
            id = ObjectId().toString(),
            name = "Project",
            description = "description",
            createdBy = "elhady",
            projectLogs = emptyList(),
            projectState = "TODO",
            taskStates = listOf("TODO", "IN_PROGRESS", "DONE"),
            projectStates = listOf("ACTIVE", "ARCHIVED"),
            matesIds = emptyList(),
            tasks = emptyList()
        )
        return try {
            collection.insertOne(projectItem)
            projectItem
        } catch (exception: Exception) {
            throw OperationFailedException("insert error : ${exception.message}")
        }
    }

    suspend fun getAllProjects(): List<ProjectDto> {
        return collection.find().toList()
    }

    override fun deleteProject(projects: List<ProjectDto>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun editProject(projects: List<ProjectDto>): Result<Unit> {
        TODO("Not yet implemented")
    }
}