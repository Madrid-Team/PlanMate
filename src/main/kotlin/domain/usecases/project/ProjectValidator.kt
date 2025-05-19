package domain.usecases.project

import domain.models.project.Project
import domain.utils.*

class ProjectValidator() {
    fun validate(project: Project) {
        validateName(project)
        validateDescription(project)
        validateStates(project)
        validateTaskStates(project)
    }

    private fun validateStates(project: Project) {
        if (project.projectStates.isEmpty()) {
            throw ProjectStatesIsEmptyException()
        }
    }

    private fun validateTaskStates(project: Project) {
        if (project.taskStates.isEmpty()) {
            throw ProjectTaskStatesIsEmptyException()
        }
    }

    fun validateName(project: Project) {
        if (project.name.isBlank()) {
            throw ProjectNameInvalidException()
        }

        if (!project.name.matches(Regex("^[A-Za-z ]+$"))) {
            throw ProjectNameInvalidException()
        }
    }

    fun validateDescription(project: Project) {
        with(project.description) {
            when {
                isBlank() -> throw ProjectDescriptionIsEmptyException()
                !matches(Regex("^[A-Za-z ]+$")) -> throw ProjectDescriptionInvalidException()
                length < 10 -> throw ProjectDescriptionTooShortException()
            }

        }

    }
}