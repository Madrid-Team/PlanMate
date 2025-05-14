package data.source.csv.user

import data.dto.authentication.UserDto

interface CurrentUserProvider {
    fun setCurrentUser(user: UserDto)
    fun getCurrentUser(): UserDto
}