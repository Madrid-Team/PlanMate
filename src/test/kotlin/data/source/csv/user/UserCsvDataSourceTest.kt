package data.source.csv.user

import com.google.common.truth.Truth.assertThat
import data.dto.authentication.UserDto
import data.mapper.toDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.utils.UserExceptions
import domain.utils.UserExist
import domain.utils.UserNotFoundException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.util.*


class UserCsvDataSourceTest {

    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var userCsvParser: UserCsvParser
    private lateinit var dataSource: UserCsvDataSource
    private lateinit var externalUserDataSource: UserExternalDataSource

    private val user1 =
        UserDto("126fb5810-951e-4913-aae8-1d36d72d85eb", "username1", "passwordhash1", UserRole.ADMIN.name)
    private val user2 =
        UserDto("26fb5810-951e-4913-aae8-1d36d72d85eb", "username2", "passwordhash2", UserRole.MATE.name)
    private val row1 = "26fb5810-951e-4913-aae8-1d36d72d85eb,username1,passwordhash1,ADMIN"
    private val row2 = "26fb5810-951e-4913-aae8-1d36d72d85eb,username2,passwordhash2,MATE"

    @BeforeEach
    fun setUp() {
        fileCsvWriter = mockk(relaxed = true)
        fileCsvReader = mockk(relaxed = true)
        userCsvParser = mockk(relaxed = true)
        externalUserDataSource = mockk(relaxed = true)
        dataSource = UserCsvDataSource(fileCsvReader, fileCsvWriter, userCsvParser)
    }

    @Test
    fun `Should create user successfully`() {
        runTest {
            // Given
            val user = User(UUID.randomUUID(), "username1", "password1", UserRole.ADMIN.name)
            val row = userCsvParser.parseUserToRow(user.toDto())
            every { fileCsvWriter.writeToCsvFile(row) } returns Unit

            coEvery { dataSource.getUserByName(user.username) } throws UserNotFoundException()
            // When & Then
            assertDoesNotThrow {
                dataSource.createNewUser(user.toDto())
            }
        }
    }

    @Test
    fun `Should fail to create user when user already exists`() {
        runTest {
            // Given
            val user = User(UUID.randomUUID(), "username1", "password1", UserRole.ADMIN.name)
            val row = userCsvParser.parseUserToRow(user.toDto())
            every { fileCsvWriter.writeToCsvFile(row) } returns Unit
            every { fileCsvReader.readCsvFile() } returns listOf(row1)
            every { userCsvParser.parseRowToUser(row1) } returns user1

            // When
            assertThrows<UserExist> {
                dataSource.createNewUser(user.toDto())
            }
            // Then
            verify(exactly = 0) { fileCsvWriter.writeToCsvFile(row) }
        }
    }

    @Test
    fun `Should fail to create user when writer throws IOException`() {
        runTest {
            // Given
            val user = User(UUID.randomUUID(), "username2", "password2", UserRole.MATE.name)
            val row = userCsvParser.parseUserToRow(user.toDto())
            val exception = IOException("File write failed")

            every { fileCsvWriter.writeToCsvFile(row) } throws UserExceptions("")

            // When
            assertThrows<UserExceptions> {
                dataSource.createNewUser(user.toDto())
            }
            // Then
        }
    }


    // region getAllUsersTest
    @Test
    fun `getAllUsers should return all users when result is success`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } returns listOf(row1, row2)
            every { userCsvParser.parseRowToUser(row1) } returns user1
            every { userCsvParser.parseRowToUser(row2) } returns user2

            // When
            // Then
            assertDoesNotThrow {
                dataSource.getAllUsers()
            }
        }
    }

    @Test
    fun `getAllUsers should return empty list when no user exist`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } returns listOf()

            // When
            // Then
            val result = dataSource.getAllUsers()
            assertDoesNotThrow {
                dataSource.getAllUsers()
            }
            assertThat(result).isEmpty()
        }
    }

    @Test
    fun `getAllUsers should throws exception when file read fails`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } throws IOException()

            // When & Then
            assertThrows<IOException> {
                dataSource.getAllUsers()
            }
        }
    }

    // endregion
    // region getUserByNameTest
    @Test
    fun `getUserByName should return user when username is exists`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } returns listOf(row1)
            every { userCsvParser.parseRowToUser(row1) } returns user1
            // When
            val result = dataSource.getUserByName(user1.username)

            // Then
            assertDoesNotThrow {
                dataSource.getUserByName(user1.username)
            }

            assertThat(result).isEqualTo(user1)
        }
    }

    @Test
    fun `getUserByName should throws exception when file read fails`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } throws IOException()

            // When & Then
            assertThrows<IOException> {
                dataSource.getUserByName("username1")
            }
        }
    }

    // endregion
    // region getUserByNameTest
    @Test
    fun `getUser should return user when id is exist`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } returns listOf(row1)
            every { userCsvParser.parseRowToUser(row1) } returns user1
            val id = user1.id
            // When
            val result = dataSource.getUserById(id)
            // Then
            assertDoesNotThrow {
                dataSource.getUserById(id)
            }
            assertThat(result).isEqualTo(user1)
        }
    }

    @Test
    fun `getUser should throws exception when file read fails`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } throws IOException()

            // When & Then
            assertThrows<IOException> {
                dataSource.getUserById("26fb5810-951e-4913-aae8-1d36d72d85eb")
            }
        }
    }
    // endregion

    // region deleteUserTest
    @Test
    fun `deleteUser should complete successfully when user is deleted`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } returns listOf(row1, row2)
            every { userCsvParser.parseRowToUser(row1) } returns user1
            every { userCsvParser.parseRowToUser(row2) } returns user2
            every { userCsvParser.parseUserToRow(user2) } returns row2
            every { fileCsvWriter.updateCsvFile(row2) } returns Unit

            // When & Then - no exception should be thrown

            assertDoesNotThrow {
                dataSource.deleteUser("26fb5810-951e-4913-aae8-1d36d72d85eb")
            }
//        verify(verifyzzzzfgdfgtfgzexactly = 1) { fileCsvWriter.updateCsvFile(row2) }
        }
    }

    @Test
    fun `deleteUser should fail when file write fails`() {
        runTest {
            coEvery { externalUserDataSource.getAllUsers() } returns emptyList()

            assertThrows<UserNotFoundException> {
                dataSource.deleteUser("non-existent-user-id")
            }
            verify(exactly = 0) { fileCsvWriter.updateCsvFile(any()) }
        }
    }

    @Test
    fun `deleteUser should throw exception when file read fails`() {
        runTest {
            // Given
            every { fileCsvReader.readCsvFile() } throws IOException("File read failed")

            // When & Then
            assertThrows<IOException> { dataSource.deleteUser("1") }
        }
    }
}