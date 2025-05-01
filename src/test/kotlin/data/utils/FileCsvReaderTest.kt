package data.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.test.Test

class FileCsvReaderTest {

    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var fileValidator: FileValidator
    private lateinit var mockFile: File


    @BeforeEach
    fun setUp() {
        mockFile = File("fake.csv")
        fileValidator = FileValidator(mockFile)
        fileCsvReader = FileCsvReader(fileValidator)
    }

    @Test
    fun `readProjectCsvFile function should return list of projects as strings`() {

        val line = "header\nrow one"
        val expectedRow = "row one"

        mockFile.writeText(line)

        val result = fileCsvReader.readCsvFile()
        assertThat(result[0]).contains(expectedRow)


    }

    @Test
    fun `readProjectCsvFile function should return IOException if file not found`() {

        mockFile.delete()

        assertThrows<IOException> {
            fileCsvReader.readCsvFile()
        }

    }


}