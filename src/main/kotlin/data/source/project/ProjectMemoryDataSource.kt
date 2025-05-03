package data.source.project

import domain.models.project.Project

class ProjectMemoryDataSource {

    private val projects = mutableListOf<Project>()

    fun getProjects(): List<Project> {
        return projects.toList()
    }

    fun setProjects(projects: List<Project>) {
          this.projects.addAll(projects)
    }

    fun addProject(project: Project) {
        projects.add(project)
    }

    fun deleteProject(projectId: String):List<Project> {
        projects.removeIf { it.id.toString() == projectId }
        return projects
    }

    fun editProject(project: Project):List<Project> {
        val updatedIndex = projects.indexOf(project)
        if (updatedIndex != -1) {
            projects[updatedIndex] = project
        } else {
            println("Project not found: $project")
        }
        return projects
    }
}
