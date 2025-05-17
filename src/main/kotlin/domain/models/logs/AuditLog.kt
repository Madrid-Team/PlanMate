package domain.models.logs

import domain.utils.convertDateToReadableDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*


data class AuditLog(
    val id: UUID = UUID.randomUUID(),
    val operationType: OperationType,
    val entityName: String,
    val entityType: EntityType,
    val entityId: String,
    val projectId: String? = null,
    val username: String,
    val fieldName: String? = null,
    val oldValue: String? = null,
    val newValue: String? = null,
    val timestamp: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        .convertDateToReadableDate()
) {
    var changeHappened = ""

    override fun toString(): String {
        if (operationType == OperationType.UPDATE) {
            changeHappened = "$fieldName from $oldValue to $newValue"
        }
        return "User $username $operationType $entityType $entityName $changeHappened at $timestamp"

    }
}

enum class EntityType {
    PROJECT,
    TASK;

    override fun toString(): String {
        return name.lowercase()
    }
    companion object {
        fun toEntityType(value: String): EntityType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown EntityType: $value")
        }
    }
}

enum class OperationType {
    CREATE,
    UPDATE,
    DELETE;

    override fun toString(): String {
        return name.lowercase()
    }
    companion object {
        fun toOperationType(value: String): OperationType {
            return OperationType.entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown EntityType: $value")
        }
    }
}