package data.utils

import io.mockk.every
import io.mockk.mockk
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


         every { fileValidator.getFile() } returns mockFile


        assertDoesNotThrow { fileCsvWriter.writeToCsvFile(row) }

    }

    @Test
    fun `writeToCsvFile function should return FileNotFoundException if file not found`() {
        val row = "test,data,row"


        every { fileValidator.getFile() } throws FileNotFoundException("File not found")


        assertThrows<FileNotFoundException> {
            fileCsvWriter.writeToCsvFile(row)
        }

    }

    @Test
    fun `writeToCsvFile function should return IOException when can't writing to CSV file`() {
        val row = "test,data,row"


        every { fileValidator.getFile() } throws IOException("Error writing to CSV file")


        assertThrows<IOException> {
            fileCsvWriter.writeToCsvFile(row)
        }

    }

}