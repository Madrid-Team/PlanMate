package data.source.user

import com.google.common.truth.Truth.assertThat
import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserCsvDataSourceTest {

    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var userCsvParser: UserCsvParser
    private lateinit var dataSource: UserCsvDataSource

    private val user1 = UserDto(
        id = "1",
        username = "username1",
        passwordHash = "passwordhash1",
        role = UserRoleDto.ADMIN,
    )
    private val user2 = UserDto(
        id = "2",
        username = "username2",
        passwordHash = "passwordhash2",
        role = UserRoleDto.MATE,
    )
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

}