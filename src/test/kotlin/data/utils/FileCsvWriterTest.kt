package data.utils

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.IOException
import kotlin.test.Test

class FileCsvWriterTest {

    private lateinit var fileValidator: FileValidator
    private lateinit var mockFile: File
    private lateinit var fileCsvWriter: FileCsvWriter

    @BeforeEach
    fun setup() {
        mockFile = File("fake.csv")
        fileValidator = mockk(relaxed = true)
        fileCsvWriter = FileCsvWriter(fileValidator)
    }


    @Test
    fun `writeToCsvFile function should appends text to file if row is correct`() {
        val row = "test,data,row"


         every { fileValidator.checkFile() } returns mockFile


        assertDoesNotThrow { fileCsvWriter.writeToCsvFile(row) }

    }


    @Test
    fun `writeToCsvFile function should return IOException when can't writing to CSV file`() {
        val row = "test,data,row"


        every { fileValidator.checkFile() } throws IOException("Error writing to CSV file")


        assertThrows<IOException> {
            fileCsvWriter.writeToCsvFile(row)
        }

    }

    @Test
    fun `updateCsvFile function should write text to file if content is correct`() {
        val row = "test,data,row"


        every { fileValidator.checkFile() } returns mockFile


        assertDoesNotThrow { fileCsvWriter.updateCsvFile(row) }

    }


    @Test
    fun `updateCsvFile function should return IOException when can't update to CSV file`() {
        val row = "test,data,row"


        every { fileValidator.checkFile() } throws IOException("Error writing to CSV file")


        assertThrows<IOException> {
            fileCsvWriter.updateCsvFile(row)
        }

    }

}