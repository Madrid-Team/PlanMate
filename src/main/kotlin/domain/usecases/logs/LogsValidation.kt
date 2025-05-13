package domain.usecases.logs

import domain.models.logs.OperationType
import domain.utils.PlanMateExceptions

class LogsValidation {
    fun validateLogs(
        entityName: String,
        username: String,
        fieldName: String = "",
        oldValue: String = "",
        newValue: String = "",
        operationType: OperationType,
    ){
        if (entityName.isBlank()) {
            throw PlanMateExceptions("Entity name cannot be blank")
        }
        if (username.isBlank()) {
            throw PlanMateExceptions("Username cannot be blank")
        }

        if (operationType == OperationType.UPDATE) {
            if (fieldName.isBlank()) {
                throw PlanMateExceptions("Field name must be specified for update operation")
            }
            if (oldValue.isBlank()) {
                throw PlanMateExceptions("Old value must be specified for update operation")
            }
            if (newValue.isBlank()) {
                throw PlanMateExceptions("New value must be specified for update operation")
            }
            if (oldValue == newValue) {
                throw PlanMateExceptions("Old and new values must be different for update operation")
            }
        }
    }
}