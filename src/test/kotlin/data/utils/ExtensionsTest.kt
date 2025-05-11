package data.utils

import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import java.io.IOException


class ThrowableExtensionsTest {

    @Test
    fun `should return ProjectsFileNotExistsException for FileNotFoundException`() {
        val exception = FileNotFoundException()
        val result = exception.toProjectException()

        assertTrue(result is ProjectExceptions.ProjectsFileNotExistsException)
    }

    @Test
    fun `should return ProjectsReadWriteException for IOException`() {
        val exception = IOException()
        val result = exception.toProjectException()

        assertTrue(result is ProjectExceptions.ProjectsReadWriteException)
    }

    @Test
    fun `should return PlanMateExceptions for unknown Throwable`() {
        val exception = IllegalArgumentException("invalid arg")
        val result = exception.toProjectException()

        assertTrue(result is PlanMateExceptions)
        assertEquals("invalid arg", result.message)
    }

    @Test
    fun `should return PlanMateExceptions with null message for null Throwable`() {
        val result = (null as Throwable?).toProjectException()

        assertTrue(result is PlanMateExceptions)
        assertEquals("null", result.message)
    }
}
