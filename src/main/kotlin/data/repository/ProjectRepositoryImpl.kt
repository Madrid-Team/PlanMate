package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.project.ExternalProjectDataSource
import data.utils.toProjectException
import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.ProjectExceptions

class ProjectRepositoryImpl(
    private val externalProjectDataSource: ExternalProjectDataSource,
) : ProjectRepository {


    override suspend fun getAllProjects(): List<Project> {
       return try {
           externalProjectDataSource.getProjects().map { it.toDomain() }
       }catch (e:Exception){
           throw e.toProjectException()
       }
    }


    override suspend fun createProject(project: Project) {
       return try {
           externalProjectDataSource.createProject(project.toDto())
       }catch (e:Exception){
           throw e.toProjectException()
       }
    }

    override suspend fun deleteProject(projectId: String) {
       return try {
           externalProjectDataSource.deleteProject(projectId)
       }catch (e:Exception){
           throw e.toProjectException()
       }
    }

    override suspend fun editProject(project: Project) {
       return try {
           externalProjectDataSource.editProject(project.toDto())
       }catch (e:Exception){
           throw e.toProjectException()
       }

    }

    override suspend fun getProjectLogsById(id: String): List<String> {
      return try {

         externalProjectDataSource.getProjectLogsById(id) ?: throw ProjectExceptions.ProjectNotFoundException()

      }catch (e:Exception){
          throw e.toProjectException()
      }
    }

    override suspend fun getProjectById(id: String): Project {
       return try {
           externalProjectDataSource.getProjectById(id)?.toDomain()  ?: throw ProjectExceptions.ProjectNotFoundException()
       }catch (e:Exception){
           throw e.toProjectException()
       }
    }

}