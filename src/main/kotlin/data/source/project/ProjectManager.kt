package data.source.project

import data.dto.project.ProjectDto

class ProjectManager {

    private val projects = mutableListOf<ProjectDto>()

    fun getProjects(): List<ProjectDto> {
        return projects.toList()
    }

    fun setProjects(projects: List<ProjectDto>) {
          this.projects.addAll(projects)
    }

    fun addProject(project: ProjectDto) {
        projects.add(project)
    }

    fun deleteProject(projectId: String):List<ProjectDto> {
        projects.removeIf { it.id == projectId }
        return projects
    }

    fun editProject(project: ProjectDto):List<ProjectDto> {
        val updatedIndex = projects.indexOfFirst { it.id == project.id }
        projects[updatedIndex] = project
        return projects
    }
}
