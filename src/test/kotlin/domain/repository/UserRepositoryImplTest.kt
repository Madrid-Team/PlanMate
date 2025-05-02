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
    private lateinit var userCsvDataSource: UserCsvDataSource
    private lateinit var userCsvParser: UserCsvParser
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        userDataSource = mockk()
        userCsvDataSource = mockk()
        userCsvParser = mockk()
        userRepositoryImpl = UserRepositoryImpl(userDataSource, userCsvDataSource,userCsvParser)
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


}