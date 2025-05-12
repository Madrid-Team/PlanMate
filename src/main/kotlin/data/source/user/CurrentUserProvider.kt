package data.source.user

import data.dto.authentication.UserDto

interface CurrentUserProvider {
    fun setCurrentUser(user: UserDto)
    fun getCurrentUser(): UserDto
}