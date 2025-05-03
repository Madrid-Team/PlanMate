package domain.usecases.logs

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.utlis.convertDateIntoReadableDate
import java.time.LocalDateTime

class CreateLogUseCase {
    fun invoke(
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
        return "User $username ${operationType.toString()} ${entityType.toString()} $entityName $changeHappened at $timestamp"
    }
}