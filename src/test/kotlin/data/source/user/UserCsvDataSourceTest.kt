package data.source.user

import com.google.common.truth.Truth.assertThat
import data.dto.authentication.UserDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.models.authentication.UserRole
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException


class UserCsvDataSourceTest {

    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var userCsvParser: UserCsvParser
    private lateinit var dataSource: UserCsvDataSource

    private val user1 = UserDto("1", "username1", "passwordhash1", UserRole.ADMIN.name)
    private val user2 = UserDto("2", "username2", "passwordhash2", UserRole.MATE.name)
    private val row1 = "1,username1,passwordhash1,ADMIN"
    private val row2 = "2,username2,passwordhash2,MATE"

    @BeforeEach
    fun setUp() {
        fileCsvWriter = mockk()
        fileCsvReader = mockk()
        userCsvParser = mockk()
        dataSource = UserCsvDataSource(fileCsvReader, fileCsvWriter, userCsvParser)
    }

    @Test
    fun `Should create user successfully`() {
        // Given
        val row = "1,username1,password1,ADMIN"

        every { fileCsvWriter.writeToCsvFile(row) } returns Unit

        // When
        val result = dataSource.createNewUser(row)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Should fail to create user when writer throws IOException`() {
        // Given
        val row = "2,username2,password2,MATE"
        val exception = IOException("File write failed")

        every { fileCsvWriter.writeToCsvFile(row) } throws exception

        // When
        val result = dataSource.createNewUser(row)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IOException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("File write failed")
    }


    // region getAllUsersTest
    @Test
    fun `getAllUsers should return all users when result is success`() {
        // Given
        every { fileCsvReader.readCsvFile() } returns listOf(row1, row2)
        every { userCsvParser.parseRowToUser(row1) } returns user1
        every { userCsvParser.parseRowToUser(row2) } returns user2

        // When
        val result = dataSource.getAllUsers()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).containsExactly(user1, user2)
    }

    @Test
    fun `getAllUsers should return empty list when no user exist`() {
        // Given
        every { fileCsvReader.readCsvFile() } returns listOf()

        // When
        val result = dataSource.getAllUsers()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun `getAllUsers should throws exception when file read fails`() {
        // Given
        every { fileCsvReader.readCsvFile() } throws Exception()

        // When
        val result = dataSource.getAllUsers()
        // When & Then
        assertThat(result.isFailure).isTrue()
    }

    // endregion
    // region getUserByNameTest
    @Test
    fun `getUserByName should return user when username is exists`() {
        // Given
        every { fileCsvReader.readCsvFile() } returns listOf(row1)
        every { userCsvParser.parseRowToUser(row1) } returns user1

        // When
        val result = dataSource.getUserByName("username1")

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(user1)
    }

    @Test
    fun `getUserByName should throws exception when file read fails`() {
        // Given
        every { fileCsvReader.readCsvFile() } throws Exception()

        // When
        val result = dataSource.getUserByName("username1")
        // When & Then
        assertThat(result.isFailure).isTrue()
    }

    // endregion
    // region getUserByNameTest
    @Test
    fun `getUser should return user when id is exist`() {
        // Given
        every { fileCsvReader.readCsvFile() } returns listOf(row1)
        every { userCsvParser.parseRowToUser(row1) } returns user1

        // When
        val result = dataSource.getUserById("1")

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(user1)
    }

    @Test
    fun `getUser should throws exception when file read fails`() {
        // Given
        every { fileCsvReader.readCsvFile() } throws Exception()

        // When
        val result = dataSource.getUserById("1")
        // When & Then
        assertThat(result.isFailure).isTrue()
    }
    // endregion

    // region deleteUserTest
    @Test
    fun `deleteUser should complete successfully when user is deleted`() {
        // Given
        every { fileCsvReader.readCsvFile() } returns listOf(row1, row2)
        every { userCsvParser.parseRowToUser(row1) } returns user1
        every { userCsvParser.parseRowToUser(row2) } returns user2
        every { userCsvParser.parseUserToRow(user2) } returns row2
        every { fileCsvWriter.updateCsvFile(row2) } returns Unit

        // When & Then - no exception should be thrown
        dataSource.deleteUser("1")
        verify(exactly = 1) { fileCsvWriter.updateCsvFile(row2) }
    }

    @Test
    fun `deleteUser should throw exception when file write fails`() {
        // Given
        every { fileCsvReader.readCsvFile() } returns listOf(row1, row2)
        every { userCsvParser.parseRowToUser(row1) } returns user1
        every { userCsvParser.parseRowToUser(row2) } returns user2
        every { userCsvParser.parseUserToRow(user2) } returns row2
        every { fileCsvWriter.updateCsvFile(row2) } throws IOException("File write failed")

        // When & Then
        assertThrows<IOException> { dataSource.deleteUser("1") }
    }

    @Test
    fun `deleteUser should throw exception when file read fails`() {
        // Given
        every { fileCsvReader.readCsvFile() } throws IOException("File read failed")

        // When & Then
        assertThrows<Exception> { dataSource.deleteUser("1") }
    }
    // endregion
}