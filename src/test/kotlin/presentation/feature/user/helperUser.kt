package presentation.feature.user

import data.dto.authentication.UserDto
import java.util.*

fun helperUser(
    id: UUID = UUID.randomUUID(),
    username: String = "user",
    passwordHash: String = "hashed_password",
    role: String = "ADMIN"
): UserDto {
    return UserDto(
        id = id.toString(),
        username = username,
        passwordHash = passwordHash,
        role = role
    )
}

