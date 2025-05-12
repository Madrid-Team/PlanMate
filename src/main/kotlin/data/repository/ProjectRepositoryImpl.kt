package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.project.ProjectExternalDataSource
import data.source.user.CurrentUserProvider
import data.utils.toProjectException
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions

class ProjectRepositoryImpl(
    private val projectExternalDataSource: ProjectExternalDataSource,
    private val currentUserProvider: CurrentUserProvider
) : ProjectRepository {


    override suspend fun getAllProjects(): List<Project> {
       return try {
           projectExternalDataSource.getProjects(currentUserProvider.getCurrentUser()).map { it.toDomain() }
       }catch (e:Exception){
           throw e.toProjectException()
       }
    }


    override suspend fun createProject(project: Project) {
       return try {
           projectExternalDataSource.createProject(project.toDto())
       }catch (e:Exception){
           throw e.toProjectException()
       }
    }

    override suspend fun deleteProject(projectId: String) {
       return try {
           projectExternalDataSource.deleteProject(projectId)
       }catch (e:Exception){
           throw e.toProjectException()
       }
    }

    override suspend fun editProject(project: Project) {
       return try {
           projectExternalDataSource.editProject(project.toDto())
       }catch (e:Exception){
           throw e.toProjectException()
       }

    }

    override suspend fun getProjectLogsById(id: String): List<String> {
      return try {

         projectExternalDataSource.getProjectLogsById(id) ?: throw ProjectExceptions.ProjectNotFoundException()

      }catch (e:Exception){
          throw e.toProjectException()
      }
    }

    override suspend fun getProjectById(id: String): Project {
       return try {
           projectExternalDataSource.getProjectById(id)?.toDomain()  ?: throw ProjectExceptions.ProjectNotFoundException()
       }catch (e:Exception){
           throw e.toProjectException()
       }
    }

}