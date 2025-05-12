package domain.usecases.project

import domain.models.project.Project
import domain.utils.ProjectExceptions

class ProjectValidator() {
    fun validate(project: Project) {
        validateName(project)
        validateDescription(project)
        validateStates(project)
        validateTaskStates(project)
    }

    private fun validateStates(project: Project) {
        if (project.projectStates.isEmpty()) {
            throw ProjectExceptions.ProjectStatesIsEmptyException()
        }
    }

    private fun validateTaskStates(project: Project) {
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

    fun validateDescription(project: Project) {
        with(project.description) {
            when {
                isBlank() -> throw ProjectExceptions.ProjectDescriptionIsEmptyException()
                !matches(Regex("^[A-Za-z ]+$")) -> throw ProjectExceptions.ProjectDescriptionInvalidException()
                length < 10 -> throw ProjectExceptions.ProjectDescriptionTooShortException()
            }

        }

    }
}