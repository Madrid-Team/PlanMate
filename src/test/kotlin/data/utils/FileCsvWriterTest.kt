package data.utils

import com.google.common.base.Verify.verify
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

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