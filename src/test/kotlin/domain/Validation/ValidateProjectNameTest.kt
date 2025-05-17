package domain.Validation

import domain.usecases.project.ProjectValidatorUseCase
import domain.utils.ProjectExceptions
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest

class ValidateProjectNameTest {
    private lateinit var projectValidatorUseCase: ProjectValidatorUseCase

    @BeforeTest
    fun setup() {
        projectValidatorUseCase = ProjectValidatorUseCase()
    }

    @Test
    fun `should not throw exception project name is valid`() {
        assertDoesNotThrow {
            projectValidatorUseCase.validateName(validProjectName)
        }
    }

    @Test
    fun `should throw exception project name is invalid`() {
        assertThrows<ProjectExceptions.ProjectNameInvalidException> {
            projectValidatorUseCase.validateName(inValidProjectName)
        }
    }

    private val validProjectName = domain.usecases.createProject(name = "project")
    private val inValidProjectName = domain.usecases.createProject(name = "####")
}