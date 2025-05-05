package domain.models.authentication

import java.util.*

data class User(
    val id: UUID,
    val username: String,
    val passwordHash: String,
    val role: String,
)