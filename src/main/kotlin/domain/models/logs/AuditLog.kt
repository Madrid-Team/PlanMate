package domain.models.logs

import domain.utils.convertDateIntoReadableDate
import java.time.LocalDateTime

data class AuditLog(
    val operationType: OperationType,
    val entityName: String,
    val entityType: EntityType,
    val username: String,
    val fieldName: String = "",
    val oldValue: String = "",
    val newValue: String = "",
    val timestamp: String = LocalDateTime.now().convertDateIntoReadableDate()
) {
    var changeHappened = ""
    override fun toString(): String {
        if (operationType == OperationType.UPDATE) {
            changeHappened = "$fieldName from $oldValue to $newValue"
        }
        return "User $username $operationType $entityType $entityName $changeHappened at $timestamp"

    }
}