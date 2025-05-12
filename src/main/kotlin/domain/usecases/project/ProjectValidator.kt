package domain.usecases.project

import domain.models.project.Project
import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions

class ProjectValidator {
    fun validate(project: Project) {
        validateName(project)

        if (project.description.isBlank()) {
            throw ProjectExceptions.ProjectDescriptionIsEmptyException()
        }

        if (project.projectStates.isEmpty()) {
            throw ProjectExceptions.ProjectStatesIsEmptyException()
        }

        if (project.taskStates.isEmpty()) {
            throw ProjectExceptions.ProjectTaskStatesIsEmptyException()
        }
    }

    fun validateName(project: Project) {
        if (project.name.isBlank()) {
            throw ProjectExceptions.ProjectNameInvalidException()
        }

        if (!project.name.matches(Regex("^[A-Za-z ]+$"))) {
            throw ProjectExceptions.ProjectNameInvalidException()
        }
    }
}