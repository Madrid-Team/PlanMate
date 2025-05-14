package data.source.user

import com.google.common.truth.Truth.assertThat
import data.dto.authentication.UserDto
import data.source.csv.user.UserCsvParser
import domain.models.authentication.UserRole
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest

class UserCsvParserTest {

    private lateinit var userCsvParser: UserCsvParser
    private lateinit var user: UserDto
    private lateinit var row: String
    private val rowTest = "1,User Name,shci58392nwsuss9203asdx,ADMIN"
    private val userTest = UserDto(
        id = "1",
        username = "User Name",
        passwordHash = "shci58392nwsuss9203asdx",
        role = UserRole.ADMIN.name,
    )

    @BeforeTest
    fun setUp() {
        userCsvParser = UserCsvParser()
        row = userCsvParser.parseUserToRow(userTest)
        user = userCsvParser.parseRowToUser(rowTest)
    }


    @Test
    fun `parseRowToUser should parse user id correctly`() {
        assertThat(user.id).contains("1")
    }

    @Test
    fun `parseRowToUser should parse user name correctly`() {
        assertThat(user.username).contains("User Name")
    }

    @Test
    fun `parseRowToUser should parse user password hash correctly`() {
        assertThat(user.passwordHash).contains("shci58392nwsuss9203asdx")
    }

    @Test
    fun `parseRowToUser should parse user role correctly`() {
        assertThat(user.role).contains("ADMIN")
    }

    @Test
    fun `parseUserToRow should parse user to row correctly`() {
        assertThat(row).isEqualTo(rowTest)
    }

}