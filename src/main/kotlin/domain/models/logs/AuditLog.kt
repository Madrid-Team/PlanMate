package domain.models.logs

import domain.utlis.convertDateIntoReadableDate
import java.time.LocalDateTime


interface AuditLogFormatter {
    fun format(
        entityName: String,
        entityType: EntityType,
        username: String,
        fieldName: String? = null,
        oldValue: String? = null,
        newValue: String? = null,
        timestamp: String = LocalDateTime.now().convertDateIntoReadableDate()
    ): String
}

object CreatedLogFormatter : AuditLogFormatter {
    override fun format(
        entityName: String,
        entityType: EntityType,
        username: String,
        fieldName: String?,
        oldValue: String?,
        newValue: String?,
        timestamp: String
    ): String {
        return "User $username create ${entityType.name} $entityName at $timestamp"
    }
}

object UpdatedLogFormatter : AuditLogFormatter {
    override fun format(
        entityName: String,
        entityType: EntityType,
        username: String,
        fieldName: String?,
        oldValue: String?,
        newValue: String?,
        timestamp: String
    ): String {
        return "User $username update ${entityType.name} $entityName $fieldName changed from $oldValue' to '$newValue' at $timestamp"
    }
}

object DeletedLogFormatter : AuditLogFormatter {
    override fun format(
        entityName: String,
        entityType: EntityType,
        username: String,
        fieldName: String?,
        oldValue: String?,
        newValue: String?,
        timestamp: String
    ): String {
        return "User $username delete ${entityType.name} $entityName at $timestamp"
    }
}