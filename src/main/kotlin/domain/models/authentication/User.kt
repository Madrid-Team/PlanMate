package domain.models.authentication

import java.util.*

data class User(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val passwordHash: String,
    val role: UserRole,
)