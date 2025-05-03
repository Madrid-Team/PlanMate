package domain.usecases.task.validation

import domain.utlis.ProjectIdIsEmptyException

class CheckProjectIdValidationUseCase() {
    operator fun invoke(projectId: String) {
        if (projectId.isEmpty()) {
            throw ProjectIdIsEmptyException()
        }
    }
}