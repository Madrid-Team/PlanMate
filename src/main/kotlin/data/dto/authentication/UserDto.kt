package data.dto.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("_id") val id: String,
    val username: String,
    val passwordHash: String,
    val role: String,
)