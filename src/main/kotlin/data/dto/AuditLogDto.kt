package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AuditLogDto(
    @SerialName("_id") val id: String,
    val operationType: String,
    val entityName: String,
    val entityType: String,
    val entityId: String, //task or project
    val projectId: String?,
    val username: String,
    val fieldName: String?,
    val oldValue: String?,
    val newValue: String?,
    val timestamp: String
)