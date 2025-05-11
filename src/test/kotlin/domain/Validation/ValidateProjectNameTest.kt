package domain.Validation

import domain.utils.ProjectExceptions
import domain.validation.ValidateProjectName
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest

class ValidateProjectNameTest {
    private lateinit var validateProjectNameUseCase: ValidateProjectName

    @BeforeTest
    fun setup() {
        validateProjectNameUseCase = ValidateProjectName()
    }

    @Test
    fun `should not throw exception project name is valid`() {
        assertDoesNotThrow {
            validateProjectNameUseCase(validProjectName)
        }
    }

    @Test
    fun `should throw exception project name is invalid`() {
        assertThrows<ProjectExceptions.ProjectNameInvalidException> {
            validateProjectNameUseCase(inValidProjectName)
        }
    }

    private val validProjectName = domain.usecases.createProject(name = "project")
    private val inValidProjectName = domain.usecases.createProject(name = "####")
}