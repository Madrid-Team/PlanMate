package domain.validation

import domain.models.project.Project
import domain.utlis.ProjectExceptions

class ValidateProjectName {
    operator fun invoke(project: Project) {
        if (!project.name.matches(Regex("^[A-Za-z ]+$"))) {
            throw ProjectExceptions.ProjectNameInvalidException()
        }
    }
}