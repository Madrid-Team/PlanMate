
package domain.models.logs

import domain.utils.convertDateToReadableDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


data class AuditLog(
    val operationType: OperationType,
    val entityName: String,
    val entityType: EntityType,
    val username: String,
    val fieldName: String = "",
    val oldValue: String = "",
    val newValue: String = "",
    val timestamp: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).convertDateToReadableDate()
) {
    var changeHappened = ""
    override fun toString(): String {
        if (operationType == OperationType.UPDATE) {
            changeHappened = "$fieldName from $oldValue to $newValue"
        }
        return "User $username $operationType $entityType $entityName $changeHappened at $timestamp"

    }
}