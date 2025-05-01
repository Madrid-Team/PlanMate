package data.source.user

import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class UserCsvDataSourceTest {

    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var userCsvParser: UserCsvParser
    private lateinit var dataSource: UserCsvDataSource

    @BeforeEach
    fun setUp() {
        fileCsvWriter = mockk()
        fileCsvReader = mockk()
        userCsvParser = mockk()
        dataSource = UserCsvDataSource(fileCsvReader, fileCsvWriter, userCsvParser)
    }


}