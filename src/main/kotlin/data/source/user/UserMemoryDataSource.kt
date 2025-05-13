package data.source.user

import data.dto.authentication.UserDto

class UserMemoryDataSource : CurrentUserProvider {
    private var user: UserDto? = null

    override fun setCurrentUser(user: UserDto) {
        this.user = user
    }

    override fun getCurrentUser(): UserDto {
        return user ?: throw IllegalStateException()
    }
}