package domain.usecases.project

import domain.usecases.createProject
import domain.utils.ProjectExceptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProjectValidatorUseCaseTest {

    private lateinit var validator: ProjectValidatorUseCase

    @BeforeEach
    fun setUp() {
        validator = ProjectValidatorUseCase()
    }

    @Test
    fun `should throw ProjectNameInvalidException when name is blank`() {
        val project = createProject(
            name = "",
            description = "Valid description",
            taskStates = listOf("To Do"),
            projectStates = listOf("Planning")
        )

        assertThrows<ProjectExceptions.ProjectNameInvalidException> {
            validator.validate(project)
        }
    }

    @Test
    fun `should throw ProjectNameInvalidException when name contains invalid characters`() {
        val project = createProject(
            name = "Project@123",
            description = "Valid description",
            taskStates = listOf("To Do"),
            projectStates = listOf("Planning")
        )

        assertThrows<ProjectExceptions.ProjectNameInvalidException> {
            validator.validate(project)
        }
    }

    @Test
    fun `should throw ProjectDescriptionIsEmptyException when description is blank`() {
        val project = createProject(
            name = "Valid Name",
            description = "",
            taskStates = listOf("To Do"),
            projectStates = listOf("Planning")
        )

        assertThrows<ProjectExceptions.ProjectDescriptionIsEmptyException> {
            validator.validate(project)
        }
    }

    @Test
    fun `should throw ProjectDescriptionInvalidException when description has invalid characters`() {
        val project = createProject(
            name = "Valid Name",
            description = "Invalid123@",
            taskStates = listOf("To Do"),
            projectStates = listOf("Planning")
        )

        assertThrows<ProjectExceptions.ProjectDescriptionInvalidException> {
            validator.validate(project)
        }
    }

    @Test
    fun `should throw ProjectDescriptionTooShortException when description is too short`() {
        val project = createProject(
            name = "Valid Name",
            description = "Short",
            taskStates = listOf("To Do"),
            projectStates = listOf("Planning")
        )

        assertThrows<ProjectExceptions.ProjectDescriptionTooShortException> {
            validator.validate(project)
        }
    }

    @Test
    fun `should throw ProjectStatesIsEmptyException when projectStates is empty`() {
        val project = createProject(
            name = "Valid Name",
            description = "Valid description",
            taskStates = listOf("To Do"),
            projectStates = emptyList()
        )

        assertThrows<ProjectExceptions.ProjectStatesIsEmptyException> {
            validator.validate(project)
        }
    }

    @Test
    fun `should throw ProjectTaskStatesIsEmptyException when taskStates is empty`() {
        val project = createProject(
            name = "Valid Name",
            description = "Valid description",
            taskStates = emptyList(),
            projectStates = listOf("Planning")
        )

        assertThrows<ProjectExceptions.ProjectTaskStatesIsEmptyException> {
            validator.validate(project)
        }
    }

    @Test
    fun `should pass validation when all fields are valid`() {
        val project = createProject(
            name = "Valid Name",
            description = "This is a valid description",
            taskStates = listOf("To Do", "Done"),
            projectStates = listOf("Planning", "In Progress")
        )

        validator.validate(project)
    }
}