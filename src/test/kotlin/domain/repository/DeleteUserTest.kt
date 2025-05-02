package domain.repository

import FakeUserRepository
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.usecases.DeleteUser
import domain.utlis.UserException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest

class DeleteUserTest {

    private lateinit var deleteUser: DeleteUser
    private lateinit var fakeUser: UserRepository

    @BeforeTest
    fun setUp() {
        fakeUser = FakeUserRepository()
        deleteUser = DeleteUser(fakeUser)
    }

    @Test
    fun `Should delete user success When requested is admin and user is exist`() {
        // Given
        val admin = User(id = "1","ADMIN_1","PASSWORD_HASH_1", UserRole.ADMIN)
        val mate = User(id = "2","MATE_1","PASSWORD_HASH_2", UserRole.MATE)

        fakeUser.addUser(admin)
        fakeUser.addUser(mate)

        // When
        val result = deleteUser.invoke(admin.id, mate.id)

        // Then
        assertTrue(result)
    }

    @Test
    fun `should throw PermissionDenied exception when requested is not admin`() {
        // Given
        val mate1 = User(id = "1","MATE_1","PASSWORD_HASH_1", UserRole.MATE)
        val mate2 = User(id = "2","MATE_2","PASSWORD_HASH_2", UserRole.MATE)

        fakeUser.addUser(mate1)
        fakeUser.addUser(mate2)

        // When && Then
        assertThrows <UserException.PermissionDenied> { deleteUser.invoke(mate1.id, mate2.id) }
    }

    @Test
    fun `should throw PermissionDenied exception when admin want to delete another admin`() {
        // Given
        val admin1 = User(id = "1","ADMIN_1","PASSWORD_HASH_1", UserRole.ADMIN)
        val admin2 = User(id = "2","ADMIN_2","PASSWORD_HASH_2", UserRole.ADMIN)

        fakeUser.addUser(admin1)
        fakeUser.addUser(admin2)

        // When && Then
        assertThrows <UserException.PermissionDenied> { deleteUser.invoke(admin1.id, admin2.id) }
    }

    @Test
    fun `should throw NotFoundUser exception when user does not exist`() {
        // Given
        val admin = User(id = "1","ADMIN","PASSWORD_HASH_1", UserRole.ADMIN)

        fakeUser.addUser(admin)

        // When && Then
        assertThrows <UserException.NotFoundUser> { deleteUser.invoke(admin.id, "2") }
    }

    @Test
    fun `should throw NotFoundUser exception when admin does not exist`() {
        // Given
        val mate = User(id = "1","MATE_1","PASSWORD_HASH_1", UserRole.ADMIN)

        fakeUser.addUser(mate)

        // When && Then
        assertThrows <UserException.NotFoundUser> { deleteUser.invoke("2", mate.id) }
    }

    @Test
    fun `should throw InvalidInput exception when user is blank`() {
        // Given
        val admin = User(id = "1","ADMIN","PASSWORD_HASH_1", UserRole.ADMIN)

        fakeUser.addUser(admin)

        // When && Then
        assertThrows <UserException.InvalidInput> { deleteUser.invoke(admin.id, " ") }
    }

    @Test
    fun `should throw InvalidInput exception when admin is blank`() {
        // Given
        val mate = User(id = "1","MATE_1","PASSWORD_HASH_1", UserRole.ADMIN)

        fakeUser.addUser(mate)

        // When && Then
        assertThrows <UserException.InvalidInput> { deleteUser.invoke(" ", mate.id) }
    }
}