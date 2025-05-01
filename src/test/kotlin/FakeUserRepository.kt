import domain.models.authentication.User
import domain.repository.UserRepository

class FakeUser : UserRepository {

    val allUsers = mutableMapOf<String, User>()

    override fun deleteUser(userId: String) {
        allUsers.remove(userId)
    }

    override fun addUser(user: User) {
        allUsers[user.id] = user
    }

    override fun getUser(userId: String): User? {
        return allUsers[userId]
    }

    override fun getALLUser(): MutableMap<String, User> {
        return allUsers
    }

    override fun loginUser(userName: String, password: String): Boolean {
        return allUsers.filter { user -> user.value.username == userName && user.value.passwordHash == password }.isNotEmpty()
    }

}