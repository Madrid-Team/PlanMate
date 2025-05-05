package data.source.user

import data.dto.authentication.UserDto
import data.utils.UserColumnsIndex


class UserCsvParser {
    val userColumnsIndex = UserColumnsIndex

    fun parseRowToUser(row: String): UserDto {
        val result = row.split(",")
        return UserDto(
            id = result[userColumnsIndex.USER_ID],
            username = result[userColumnsIndex.USER_NAME],
            passwordHash = result[userColumnsIndex.USER_PASSWORD_HASH],
            role = result[userColumnsIndex.USER_ROLE],
        )
    }

    fun parseUserToRow(user: UserDto): String {
        val userCsvLine = listOf(
            user.id,
            user.username,
            user.passwordHash,
            user.role,
        ).joinToString(",")

        return userCsvLine
    }
}