package data.utils

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException
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
        val result = fileValidator.getFile()
        assertEquals(file,result )

    }

    @Test
    fun `getFile function should throw FileNotFoundException if it not exists`() {

        every { file.exists() } returns false

        assertThrows<FileNotFoundException>{
            fileValidator.getFile()
        }

    }
}




















