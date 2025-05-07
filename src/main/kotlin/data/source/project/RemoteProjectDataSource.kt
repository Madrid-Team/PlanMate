package org.madrid.data.source.project

import data.dto.project.ProjectDto

interface RemoteProjectDataSource {

    suspend fun getProjects(): List<ProjectDto>
    suspend fun createProject(project: ProjectDto)
    suspend fun deleteProject(projectId: String)
    suspend fun editProject(project: ProjectDto)
}