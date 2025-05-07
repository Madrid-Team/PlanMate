package org.madrid.data.source.project

import data.dto.project.ProjectDto

interface RemoteProjectDataSource {

    fun getProjects(): List<ProjectDto>
    fun createProject(project: ProjectDto)
    fun deleteProject(projectId: String)
    fun editProject(project: ProjectDto)
}