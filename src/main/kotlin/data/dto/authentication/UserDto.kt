package data.dto.authentication

import java.util.*

data class UserDto(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val passwordHash: String,
    val role: UserRoleDto,
)