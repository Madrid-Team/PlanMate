package data.utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.test.Test

class FileValidatorTest {

    private lateinit var file: File
    private lateinit var fileValidator: FileValidator

    @BeforeEach
    fun setUp() {
        file = mockk(relaxed = true)
        fileValidator = FileValidator(file)
    }

    @Test
    fun `getFile function should return file if it exists`() {

        every { file.exists() } returns true
        val result = fileValidator.checkFile()
        assertEquals(file,result )

    }

    @Test
    fun `getFile function should create make dir if it not exists`() {
        val result = fileValidator.createCsvFileWithHeader()
        every { !result.parentFile.exists() } returns true


        verify { file.parentFile.mkdirs() }
        assertFalse { result.exists() }

    }
    @Test
    fun `getFile function should create new file if it not exists`() {

        every { file.exists() } returns false


        val result = fileValidator.createCsvFileWithHeader()
        assertEquals(file,result )

    }
    @Test
    fun `createCsvFileWithHeader function should throw IOException if can't read or write file`() {

        every {  fileValidator.createCsvFileWithHeader() }  throws IOException()

        assertThrows<IOException>{
            fileValidator.createCsvFileWithHeader()
        }

    }
}




















