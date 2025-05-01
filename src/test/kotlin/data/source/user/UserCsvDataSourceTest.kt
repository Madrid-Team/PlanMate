package data.source.user

import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

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

}