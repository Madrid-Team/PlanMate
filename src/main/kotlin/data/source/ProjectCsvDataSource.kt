package data.source

import domain.models.project.Project

class ProjectCsvDataSource(
) : ProjectDataSource {

    override fun createProject(project: Project): Result<Unit> {
        TODO()
    }
}