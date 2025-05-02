package domain.models.logs

import domain.models.authentication.User

object CurrentUser {
    private lateinit var user:User

    fun setCurrentUser(user: User) {
        this.user = user
    }

    fun getCurrentUser():User? {
        return user
    }

}