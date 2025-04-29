package domain.models.logs

import domain.utlis.convertDateIntoReadableDate
import java.time.LocalDateTime
import java.util.*


data class AuditLog(
    val id: String = UUID.randomUUID().toString(),
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
    fun toLog():String{
        return "user $username $changeType $entityType $entityName from $oldValue to $newValue at ${timestamp.convertDateIntoReadableDate()}"
    }

}