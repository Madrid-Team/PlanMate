package presentation.feature.projects

import domain.usecases.project.DeleteProjectUseCase
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class DeleteProjectCLITest {

    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val deleteProjectUseCase = mockk<DeleteProjectUseCase>()

    private lateinit var cli: DeleteProjectCLI

    @BeforeEach
    fun setUp() {
        cli = DeleteProjectCLI(inputReader, outputPrinter, deleteProjectUseCase)
    }

    @Test
    fun `should delete project when user confirms`() = runTest {
        // Given
        every { inputReader.readInput("Enter project ID to delete: ") } returns "project-123"
        every { inputReader.readInput("Are you sure you want to delete this project? (yes/no): ") } returns "yes"
        coEvery { deleteProjectUseCase.deleteProject("project-123") } just runs

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Project deleted successfully.") }
        coVerify { deleteProjectUseCase.deleteProject("project-123") }
    }

    @Test
    fun `should cancel deletion when user does not confirm`() = runTest {
        // Given
        every { inputReader.readInput("Enter project ID to delete: ") } returns "project-123"
        every { inputReader.readInput("Are you sure you want to delete this project? (yes/no): ") } returns "no"

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Deletion cancelled.") }
        coVerify(exactly = 0) { deleteProjectUseCase.deleteProject(any()) }
    }

    @Test
    fun `should show error message when project not found`() = runTest {
        // Given
        every { inputReader.readInput("Enter project ID to delete: ") } returns "invalid-id"
        every { inputReader.readInput("Are you sure you want to delete this project? (yes/no): ") } returns "yes"
        coEvery { deleteProjectUseCase.deleteProject("invalid-id") } throws ProjectExceptions.ProjectNotFoundException()

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage(ProjectExceptions.ProjectNotFoundException().message!!) }
    }

    @Test
    fun `should show error message when other exception occurs`() = runTest {
        // Given
        every { inputReader.readInput("Enter project ID to delete: ") } returns "error-id"
        every { inputReader.readInput("Are you sure you want to delete this project? (yes/no): ") } returns "yes"
        coEvery { deleteProjectUseCase.deleteProject("error-id") } throws Exception("Unexpected error")

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Failed to delete project: Unexpected error") }
    }
}