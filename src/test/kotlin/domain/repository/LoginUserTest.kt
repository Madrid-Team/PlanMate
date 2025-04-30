package domain.repository

import FakeUser
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.usecases.LoginUserUseCase
import domain.utlis.UserException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest

class LoginUserTest {
    private lateinit var loginUser: LoginUserUseCase
    private lateinit var fakeUser: UserRepository

    @BeforeTest
    fun setUp() {
        fakeUser = FakeUser()
        loginUser = LoginUserUseCase(fakeUser)
    }
    @Test
    fun `Should login user success When username and password is true`() {
        // Given
        val admin = User(id = "1","ADMIN_1","PASSWORD_HASH_1", UserRole.ADMIN)
        val mate = User(id = "2","MATE_1","PASSWORD_HASH_2", UserRole.MATE)

        fakeUser.addUser(admin)
        fakeUser.addUser(mate)

        // When
        val result = loginUser.invoke("ADMIN_1","PASSWORD_HASH_1" )

        // Then
        assertTrue(result)
    }
    @Test
    fun `Should not login When userName or Password are incorrect`(){
        // Given
        val mate = User(id = "1","MATE_1","PASSWORD_HASH_1", UserRole.MATE)

        fakeUser.addUser(mate)

        // When && Then
        assertThrows <UserException.WrongPasswordOrUserName> { loginUser.invoke("MA", mate.id) }
    }
    @Test
    fun `Shouldn't login When username format is incorrect`(){
        // Given
        val mate = User(id = "1","MATE_1","PASSWORD_HASH_1", UserRole.MATE)

        fakeUser.addUser(mate)

        // When && Then
        assertThrows <UserException.InvalidUserNameFormat> { loginUser.invoke("1#MATE_1", "PASSWORD_HASH_1") }
    }




}