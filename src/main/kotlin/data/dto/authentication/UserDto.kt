package data.dto.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class UserDto(
    @BsonId @SerialName("_id") val id: String,
    val username: String,
    val passwordHash: String,
    val role: String,
)