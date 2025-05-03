package data.dto.authentication

import java.util.*

data class UserDto(
    val id: String,
    val username: String,
    val passwordHash: String,
    val role: String,
)