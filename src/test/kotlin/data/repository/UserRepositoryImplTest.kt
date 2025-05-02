package domain.repository

import com.google.common.truth.Truth.assertThat
import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import data.repository.UserRepositoryImpl
import data.source.user.UserCsvDataSource
import data.source.user.UserCsvParser
import data.source.user.UserDataSource
import domain.utlis.UserException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class UserRepositoryImplTest {

    private lateinit var userDataSource: UserDataSource
    private lateinit var userCsvParser: UserCsvParser
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        userDataSource = mockk()
        userCsvParser = mockk()
        userRepositoryImpl = UserRepositoryImpl(userDataSource,userCsvParser)
    }
    @Test
    fun `Should add user successfully`() {
        // Given
        val user = UserDto("1", "username1", "hash", UserRoleDto.ADMIN)
        val userRow = "1,username1,hash,ADMIN"

        every { userCsvParser.parseUserToRow(user) } returns userRow
        every { userDataSource.createUser(userRow) } returns Result.success(Unit)

        // When
        val result = userRepositoryImpl.addUser(user)

        // Then
        assertThat(result.isSuccess).isTrue()
    }
    @Test
    fun `Should fail to add user when datasource fails`() {
        // Given
        val user = UserDto("2", "username2", "hash2", UserRoleDto.MATE)
        val userRow = "2,username2,hash2,MATE"

        every { userCsvParser.parseUserToRow(user) } returns userRow
        every { userDataSource.createUser(userRow) } returns Result.failure(UserException.UserExist("User already exists"))

        // When
        val result = userRepositoryImpl.addUser(user)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(UserException.UserExist::class.java)
    }

    @Test
    fun `getUser should return user when found`() {
        // Given
        val user = UserDto("1", "username1", "passwordhash1", UserRoleDto.MATE)

        every { userDataSource.getUser("1") } returns Result.success(user)

        // When
        val result = userRepositoryImpl.getUser("1")

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(user)
    }

    @Test
    fun `getAllUsers should return user when found`() {
        // Given
        val users = listOf(
            UserDto("1", "username1", "passwordhash1", UserRoleDto.MATE),
            UserDto("2", "username2", "passwordhash2", UserRoleDto.MATE)
        )

        every { userDataSource.getAllUsers() } returns Result.success(users)

        // When
        val result = userRepositoryImpl.getAllUsers()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).containsExactlyElementsIn(users)
    }

    @Test
    fun `getUserByName should return user when found`() {
        // Given
        val user = UserDto("1", "username1", "passwordhash1", UserRoleDto.MATE)

        every { userDataSource.getUserByName("username1") } returns Result.success(user)

        // When
        val result = userRepositoryImpl.getUserByName("username1")

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(user)
    }

    @Test
    fun `Should delete user successfully`() {
        // Given
        val userId = "1"
        every { userDataSource.deleteUser(userId) } returns Result.success(Unit)

        // When
        val result = userRepositoryImpl.deleteUser(userId)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Should fail to delete user when datasource fails`() {
        // Given
        val userId = "2"
        every { userDataSource.deleteUser(userId) } returns Result.failure(UserException.NotFoundUser("User not found"))

        // When
        val result = userRepositoryImpl.deleteUser(userId)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(UserException.NotFoundUser::class.java)
    }
}