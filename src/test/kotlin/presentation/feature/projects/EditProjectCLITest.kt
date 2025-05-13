package presentation.feature.projects

import com.google.common.truth.Truth.assertThat
import domain.usecases.project.EditProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.chooseFieldsEditProject
import presentation.utils.editProjectHeader
import presentation.utils.enterProjectIdToEdit
import presentation.utils.selectOption
import presentation.utils.selectionFour
import presentation.utils.selectionOne
import kotlin.test.Test

class EditProjectCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val editProjectUseCase = mockk<EditProjectUseCase>()
    private val getProjectByIdUseCase = mockk<GetProjectByIdUseCase>()

    private lateinit var cli: EditProjectCLI


    val errorMessage = "Project error"
    private val projectId = "00000000-0000-0000-0000-000000000001"
    val originalProject = helperProject(
        id = "00000000-0000-0000-0000-000000000001",
        name = "original-name",
        description = "original-description",
        createdBy = "test-user",
        projectState = "TODO"
    )

    @BeforeEach
    fun setUp() {
        cli = EditProjectCLI(
            inputReader,
            outputPrinter,
            editProjectUseCase,
            getProjectByIdUseCase
        )
    }

    @Test
    fun `should Finish editProject when user selects 4 and return no changes`() {
        runTest {
            // Given
            every { inputReader.readInput(String.enterProjectIdToEdit) } returns projectId
            every { inputReader.readInput(String.selectOption) } returns String.selectionFour
            coEvery { getProjectByIdUseCase.getById(projectId) } returns originalProject


            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMessage(String.editProjectHeader)
                outputPrinter.printMessage(String.chooseFieldsEditProject)
                outputPrinter.printMessage("No changes were made to the project.")
            }
            coVerify { getProjectByIdUseCase.getById(projectId) }
            assertThat(cli.hasChanges).isFalse()
        }
    }

    @Test
    fun `should throws ProjectExceptions when project id not found`() {
        runTest {
            // Given
            every { inputReader.readInput(String.enterProjectIdToEdit) } returns projectId
            coEvery { getProjectByIdUseCase.getById(projectId) } throws ProjectExceptions(errorMessage)

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMessage(String.editProjectHeader)
                outputPrinter.printMessage(errorMessage)
            }
            assertThat(cli.hasChanges).isFalse()
        }
    }

    @Test
    fun `should editProjectName success when new name is valid`() {
        runTest {
            // Given
            val newName = "New Project Name"
            val updatedProject = originalProject.copy(name = newName)

            every { inputReader.readInput("Enter the new name:") } returns newName
            coEvery { editProjectUseCase.editProject(updatedProject) } just runs

            // When
            cli.editProjectName(originalProject)

            // Then
            verify { outputPrinter.printMessage("Project name updated successfully.") }
            assertThat(cli.hasChanges).isTrue()
        }
    }

    @Test
    fun `should editProjectName fail when new name is invalid`() {
        runTest {
            // Given
            val newName = "New Project Name"
            val updatedProject = originalProject.copy(name = newName)

            every { inputReader.readInput("Enter the new name:") } returns newName
            coEvery { editProjectUseCase.editProject(updatedProject) } throws ProjectExceptions(errorMessage)

            // When
            cli.editProjectName(originalProject)

            // Then
            verify { outputPrinter.printMessage("Failed to update project description: $errorMessage") }
        }
    }

    @Test
    fun `should editProjectDescription success when new Description is valid`() {
        runTest {
            // Given
            val newDescription = "New Project Description"
            val updatedProject = originalProject.copy(description = newDescription)

            every { inputReader.readInput("Enter the new description:") } returns newDescription
            coEvery { editProjectUseCase.editProject(updatedProject) } just runs

            // When
            cli.editProjectDescription(originalProject)

            // Then
            verify {outputPrinter.printMessage("Project description updated successfully.") }
            assertThat(cli.hasChanges).isTrue()
        }
    }

    @Test
    fun `should editProjectDescription fail when new Description is invalid`() {
        runTest {
            // Given
            val newName = "New Project Name"
            val updatedProject = originalProject.copy(description = newName)

            every { inputReader.readInput("Enter the new description:") } returns newName
            coEvery { editProjectUseCase.editProject(updatedProject) } throws ProjectExceptions(errorMessage)

            // When
            cli.editProjectDescription(originalProject)

            // Then
            verify { outputPrinter.printMessage("Failed to update project description: $errorMessage") }
        }
    }

    @Test
    fun `should editProjectStates success when new State is valid`() {
        runTest {
            // Given
            val newState = "ARCHIVED"
            val updatedProject = originalProject.copy(projectState = newState)
            val expectedState = "Select project State:\n1. ACTIVE\n2. ARCHIVED\nEnter number: "
            every { inputReader.readInput(expectedState) } returns "2"
            coEvery { editProjectUseCase.editProject(updatedProject) } just runs

            // When
            cli.editProjectStates(originalProject)

            // Then
            verify {
                outputPrinter.printMessage("Project state updated successfully.")
            }
            assertThat(cli.hasChanges).isTrue()
        }
    }
    @Test
    fun `should editProjectStates fail when new State is invalid`() {
        runTest {
            // Given
            val newState = "ARCHIVED"
            val updatedProject = originalProject.copy(projectState = newState)
            val expectedState = "Select project State:\n1. ACTIVE\n2. ARCHIVED\nEnter number: "
            every { inputReader.readInput(expectedState) } returns "2"
            coEvery { editProjectUseCase.editProject(updatedProject) } throws ProjectExceptions(errorMessage)

            // When
            cli.editProjectStates(originalProject)

            // Then
            verify { outputPrinter.printMessage("Failed to update project state: $errorMessage") }
        }
    }

    @Test
    fun `should editProjectStates fail when invalid selection`() {
        runTest {
            // Given
            val newState = "ARCHIVED"
            val updatedProject = originalProject.copy(projectState = newState)
            val expectedState = "Select project State:\n1. ACTIVE\n2. ARCHIVED\nEnter number: "
            every { inputReader.readInput(expectedState) } returnsMany listOf("99", "2")
            coEvery { editProjectUseCase.editProject(updatedProject) } just runs

            // When
            cli.editProjectStates(originalProject)

            // Then
            verify(exactly = 2) { inputReader.readInput(expectedState) }
            verify {
                println("Invalid selection. Please enter a valid state id ")
                outputPrinter.printMessage("Project state updated successfully.")
            }
            assertThat(cli.hasChanges).isTrue()
        }
    }

    @Test
    fun `should Finish editProject when user selects 4 and return changes success`() {
        runTest {
            // Given
            val newName = "New Project Name"
            val updatedProject = originalProject.copy(name = newName)

            every { inputReader.readInput("Enter the new name:") } returns newName
            coEvery { editProjectUseCase.editProject(updatedProject) } just runs
            every { inputReader.readInput(String.enterProjectIdToEdit) } returns projectId
            coEvery { getProjectByIdUseCase.getById(projectId) } returns originalProject
            every { inputReader.readInput(String.selectOption) } returnsMany listOf(String.selectionOne, String.selectionFour)

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMessage(String.editProjectHeader)
                outputPrinter.printMessage("Project name updated successfully.")
                outputPrinter.printMessage("Project edited successfully.")
            }

            assertThat(cli.hasChanges).isTrue()
        }
    }

    @Test
    fun `should show invalid options`() {
        runTest {
            // Given
            val newName = "New Project Name"
            val updatedProject = originalProject.copy(name = newName)

            every { inputReader.readInput("Enter the new name:") } returns newName
            coEvery { editProjectUseCase.editProject(updatedProject) } just runs
            every { inputReader.readInput(String.enterProjectIdToEdit) } returns projectId
            coEvery { getProjectByIdUseCase.getById(projectId) } returns originalProject
            every { inputReader.readInput(String.selectOption) } returnsMany listOf("5", String.selectionFour)

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMessage(String.editProjectHeader)
                outputPrinter.printMessage(String.chooseFieldsEditProject)
                outputPrinter.printMessage("Invalid option. Please try again.")
                outputPrinter.printMessage("No changes were made to the project.")
            }

            assertThat(cli.hasChanges).isFalse()
        }
    }

}