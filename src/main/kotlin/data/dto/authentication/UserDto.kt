package data.dto.authentication

data class UserDto(
    val id: String,
    val username: String,
    val passwordHash: String,
    val role: String,
)