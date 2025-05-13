package data

import data.dto.authentication.UserDto
import java.util.UUID

fun createUserDto(
    id: String = UUID.randomUUID().toString(),
    username: String = "",
    passwordHash: String = "",
    role: String = ""
) = UserDto(
    id = id,
    username = username,
    passwordHash = passwordHash,
    role = role
)