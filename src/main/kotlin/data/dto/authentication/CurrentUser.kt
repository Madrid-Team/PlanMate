package data.dto.authentication

import data.source.user.CurrentUserProvider

class CurrentUser : CurrentUserProvider {
    private var user: UserDto? = null

    override fun setCurrentUser(user: UserDto) {
        this.user = user
    }

    override fun getCurrentUser(): UserDto {
        return user ?: throw IllegalStateException()
    }
}