package data.repository

import com.google.common.truth.Truth.assertThat
import data.createUserDto
import data.mapper.toDomain
import data.source.csv.user.CurrentUserProvider
import data.source.UserExternalDataSource
import domain.utils.UserExceptions
import domain.utils.UserExceptions.UserExist
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class UserRepositoryImplTest {

    private lateinit var userExternalDataSource: UserExternalDataSource
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private lateinit var testScope: TestScope
    private lateinit var currentUserProvider: CurrentUserProvider

    @BeforeEach
    fun setUp() {
        userExternalDataSource = mockk(relaxed = true)
        testScope = TestScope()
        currentUserProvider = mockk(relaxed = true)
        userRepositoryImpl = UserRepositoryImpl(userExternalDataSource, currentUserProvider)
    }

    @Test
    fun `Should add user successfully`() {
        runTest {
            coEvery { userExternalDataSource.createNewUser(userDto) } returns Unit

            assertDoesNotThrow {
                userRepositoryImpl.createNewUser(userDto.toDomain())
            }

            coVerify { userExternalDataSource.createNewUser(userDto) }
        }
    }

    @Test
    fun `Should fail to add user when datasource fails`() {
        runTest {
            val exception = UserExist()
            coEvery { userExternalDataSource.createNewUser(userDto) } throws exception

            val thrownException = assertThrows<UserExist> {
                userRepositoryImpl.createNewUser(userDto.toDomain())
            }

            assertThat(thrownException).isSameInstanceAs(exception)
        }
    }

    @Test
    fun `getUser should return user when found`() {
        runTest {
            coEvery { userExternalDataSource.getUserById(userDto.id) } returns userDto

            val result = userRepositoryImpl.getUserByUserId(userDto.id)

            assertThat(result).isEqualTo(userDto.toDomain())
        }
    }

    @Test
    fun `getAllUsers should return user when found`() {
        runTest {
            coEvery { userExternalDataSource.getAllUsers() } returns usersDto

            val result = userRepositoryImpl.getAllUsers()

            assertThat(result).containsExactlyElementsIn(usersDto.map { it.toDomain() })
        }
    }

    @Test
    fun `getUserByName should return user when found`() {
        runTest {
            coEvery { userExternalDataSource.getUserByName(userDto.username) } returns userDto

            val result = userRepositoryImpl.getUserByName(userDto.username)

            assertThat(result).isEqualTo(userDto.toDomain())
        }
    }

    @Test
    fun `Should delete user successfully`() {
        runTest {
            coEvery { userExternalDataSource.deleteUser(userDto.id) } returns Unit

            userRepositoryImpl.deleteUserByUserId(userDto.id)

            coVerify { userExternalDataSource.deleteUser(userDto.id) }
        }
    }

    @Test
    fun `Should propagate exception from data source when deleting user`() {
        runTest {
            val exception = UserExceptions.UserNotFoundException()
            coEvery { userExternalDataSource.deleteUser(userDto.id) } throws exception

            val thrownException = assertThrows<UserExceptions.UserNotFoundException> {
                userRepositoryImpl.deleteUserByUserId(userDto.id)
            }

            assertThat(thrownException).isSameInstanceAs(exception)
        }
    }

    @Test
    fun `login should return user when login successfully`() {
        runTest {
            coEvery { userExternalDataSource.login(userDto.username, userDto.passwordHash) } returns userDto
            coEvery { currentUserProvider.setCurrentUser(userDto) } returns Unit

            val result = userRepositoryImpl.login(userDto.username, userDto.passwordHash)

            assertThat(result).isEqualTo(userDto.toDomain())
        }
    }

    @Test
    fun `login should throw UserNotFoundException when user is equal null`() {
        runTest {
            coEvery { userExternalDataSource.login(userDto.username, userDto.passwordHash) } returns null
            coEvery { currentUserProvider.setCurrentUser(userDto) } returns Unit

            assertThrows<UserExceptions.UserNotFoundException> {
                userRepositoryImpl.login(
                    userDto.username,
                    userDto.passwordHash
                )
            }
        }
    }

    @Test
    fun `login should throw UserException when catch exception`() {
        runTest {
            coEvery {
                userExternalDataSource.login(
                    userDto.username,
                    userDto.passwordHash
                )
            } throws UserExceptions.UserNotFoundException()

            coEvery { currentUserProvider.setCurrentUser(userDto) } returns Unit

            assertThrows<UserExceptions.UserNotFoundException> {
                userRepositoryImpl.login(
                    userDto.username,
                    userDto.passwordHash
                )
            }
        }
    }

    private val userDto = createUserDto(
        id = "26fb5810-951e-4913-aae8-1d36d72d85eb",
        username = "user name",
        passwordHash = "pass",
        role = "role"
    )

    private val usersDto = listOf(userDto)
}