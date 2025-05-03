package domain.usecases.task.validation

import domain.utlis.ProjectIdIsEmptyException

class CheckProjectIdUseCase() {
    operator fun invoke(projectId: String) {
        if (projectId.isEmpty()) {
            throw ProjectIdIsEmptyException()
        }
    }
}