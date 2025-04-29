import domain.models.authentication.User
import domain.repository.UserRepository

class FakeUserRepository : UserRepository {

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
}