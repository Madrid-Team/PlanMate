package data.source.project

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.authentication.UserDto
import data.dto.project.ProjectDto
import data.utils.CREATED_BY
import data.utils.MATES_USER_NAMES
import data.utils.PROJECT_ID
import domain.models.authentication.UserRole
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.conversions.Bson

class ProjectMongoDBDataSource(
    private val collection: MongoCollection<ProjectDto>
) : ProjectExternalDataSource {

    override suspend fun getProjects(user: UserDto): List<ProjectDto> {
        val filter = getRequiredFilterBasedOnUserRole(user)
        return collection.find(filter).toList()
    }

    private fun getRequiredFilterBasedOnUserRole(user: UserDto): Bson {
        return if (user.role == UserRole.ADMIN.name) {
            eq(CREATED_BY, user.username)
        } else {
            eq(MATES_USER_NAMES, user.username)
        }
    }

    override suspend fun createProject(project: ProjectDto) {
        collection.insertOne(project)
    }

    override suspend fun deleteProject(projectId: String) {
        val query = eq(PROJECT_ID, projectId)
        collection.deleteOne(query)
    }

    override suspend fun editProject(project: ProjectDto) {

        val query = eq(PROJECT_ID, project.id)

        collection.replaceOne(query, project)
    }

    override suspend fun getProjectLogsById(id: String): List<String> {
        val filter = eq(PROJECT_ID, id)
        return collection.find(filter).toList().flatMap { it.projectLogs }
    }

    override suspend fun getProjectById(id: String): ProjectDto? {
        val filter = eq(PROJECT_ID, id)
        return collection.find(filter).firstOrNull()
    }

}