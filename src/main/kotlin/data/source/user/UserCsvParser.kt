package data.source.user

import data.dto.authentication.User
import data.dto.authentication.UserRole


class UserCsvParser {

    fun parseRowToUser(row: String): User {
        // TODO parse row to User data class
        return User("", "", "", UserRole.MATE)
    }

    fun parseUserToRow(user: User): String {
        // TODO parse User data class to row
        return ""
    }
}