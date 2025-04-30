package data.dto.logs

import domain.utlis.convertDateIntoReadableDate
import java.time.LocalDateTime


data class AuditLog(
    val entityId: String,
    val entityName: String,
    val entityType: EntityType,
    val userId: String,
    val username: String,
    val changeType: ChangeType,
    val fieldName: String,
    val oldValue: String?,
    val newValue: String?,
    val timestamp: LocalDateTime = LocalDateTime.now()
){
   override fun toString(): String {
        return "user $username $changeType $entityType $entityName from $oldValue to $newValue at ${timestamp.convertDateIntoReadableDate()}"
    }

}
