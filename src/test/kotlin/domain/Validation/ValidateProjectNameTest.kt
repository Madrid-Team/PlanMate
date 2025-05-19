package domain.Validation

import domain.usecases.project.ProjectValidator
import domain.utils.ProjectNameInvalidException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest

class ValidateProjectNameTest {
    private lateinit var projectValidator: ProjectValidator

    @BeforeTest
    fun setup() {
        projectValidator = ProjectValidator()
    }

    @Test
    fun `should not throw exception project name is valid`() {
        assertDoesNotThrow {
            projectValidator.validateName(validProjectName)
        }
    }

    @Test
    fun `should throw exception project name is invalid`() {
        assertThrows<ProjectNameInvalidException> {
            projectValidator.validateName(inValidProjectName)
        }
    }

    private val validProjectName = domain.usecases.createProject(name = "project")
    private val inValidProjectName = domain.usecases.createProject(name = "####")
}