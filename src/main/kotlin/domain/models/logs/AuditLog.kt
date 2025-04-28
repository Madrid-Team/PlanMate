package domain.models.logs

import java.time.LocalDateTime
import java.util.*


data class AuditLog(
    val id: String = UUID.randomUUID().toString(),
    val entityId: String,
    val entityType: EntityType,
    val userId: String,
    val changeType: ChangeType,
    val fieldName: String,
    val oldValue: String?,
    val newValue: String?,
    val timestamp: LocalDateTime = LocalDateTime.now()
)