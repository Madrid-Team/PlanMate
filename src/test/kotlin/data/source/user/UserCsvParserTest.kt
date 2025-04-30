package data.source.user

import data.dto.authentication.User
import kotlin.test.BeforeTest

class UserCsvParserTest {

    private lateinit var userCsvParser: UserCsvParser
    private lateinit var user: User


    @BeforeTest
    fun setUp() {
        userCsvParser = UserCsvParser()
        val row = "1,User Name,Password hash,ADMIN"
        user = userCsvParser.parseRowToUser(row)
    }


}