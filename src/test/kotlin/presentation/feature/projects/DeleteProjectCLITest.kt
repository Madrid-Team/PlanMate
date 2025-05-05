package presentation.feature.projects

import domain.models.project.Project
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class DeleteProjectCLITest {

    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val deleteUseCase = mockk<DeleteProjectUseCase>()
    private val getProjectByIdUseCase = mockk<GetProjectByIdUseCase>()
    private val mockProject = mockk<Project>()

    private lateinit var cli: DeleteProjectCLI

    @BeforeEach
    fun setUp() {
        cli = DeleteProjectCLI(inputReader, outputPrinter, deleteUseCase, getProjectByIdUseCase)
    }

    @Test
    fun `show function should print Project deleted successfully when project deleted`() {
        // Given
        every { inputReader.readInput(any()) } returns "1" andThen "yes"
        every { getProjectByIdUseCase.invoke("1") } returns Result.success(mockProject)
        every { deleteUseCase.deleteProject("1") } returns Result.success(Unit)

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Project deleted successfully.") }
    }

    @Test
    fun `show function should print Deletion cancelled when cancel project deletion`() {
        // Given
        every { inputReader.readInput(any()) } returns "2" andThen "no"
        every { getProjectByIdUseCase.invoke("2") } returns Result.success(mockProject)

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Deletion cancelled.") }
        verify(exactly = 0) { deleteUseCase.deleteProject(any()) }
    }

    @Test
    fun `show function should print Failed to delete project Not found when can't delete project`() {
        // Given
        every { inputReader.readInput(any()) } returns "3" andThen "yes"
        every { getProjectByIdUseCase.invoke("3") } returns Result.success(mockProject)
        every { deleteUseCase.deleteProject("3") } returns Result.failure(Exception("Not found"))

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Failed to delete project: Not found") }
    }

    @Test
    fun `show function should print Project not found when project not found`() {
        // Given
        every { inputReader.readInput(any()) } returns "4"
        every { getProjectByIdUseCase.invoke("4") } returns Result.failure(Exception("Project not found"))

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Project not found") }
        verify(exactly = 0) { deleteUseCase.deleteProject(any()) }
    }
}