package data.source.user

import data.dto.authentication.User
import data.dto.authentication.UserRole
import data.utils.PlanMateColumnIndex.UserColumnsIndex


class UserCsvParser {
    val userColumnsIndex = UserColumnsIndex

    fun parseRowToUser(row: String): User {
        val result = row.split(",")
        return User(
            id = result[userColumnsIndex.USER_ID],
            username = result[userColumnsIndex.USER_NAME],
            passwordHash = result[userColumnsIndex.USER_PASSWORD_HASH],
            role = UserRole.valueOf(result[userColumnsIndex.USER_ROLE]),
        )
    }

    fun parseUserToRow(user: User): String {
        val userCsvLine = listOf(
            user.id,
            user.username,
            user.passwordHash,
            user.role.name,
        ).joinToString(",")

        return userCsvLine
    }
}