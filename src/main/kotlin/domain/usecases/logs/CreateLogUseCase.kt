package domain.usecases.logs

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.utils.convertDateIntoReadableDate
import java.time.LocalDateTime

class CreateLogUseCase {
     fun createLog(
        operationType: OperationType,
        entityName: String,
        entityType: EntityType,
        username: String,
        fieldName: String = "",
        oldValue: String = "",
        newValue: String = "",
        timestamp: String = LocalDateTime.now().convertDateIntoReadableDate()
    ): String {
        var changeHappened = ""
        if (operationType == OperationType.UPDATE) {
            changeHappened = "$fieldName from $oldValue to $newValue"
        }
        return "User $username $operationType $entityType $entityName $changeHappened at $timestamp"
    }
}

// User-israa-create-project-gdggdgdgd-at 2025/05/12 7:25 PM