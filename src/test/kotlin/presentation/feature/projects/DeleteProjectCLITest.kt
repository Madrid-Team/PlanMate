package presentation.feature.projects

import domain.models.project.Project
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utlis.ProjectExceptions
import io.mockk.*
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
        cli = DeleteProjectCLI(inputReader, outputPrinter, deleteUseCase)
    }

    @Test
    fun `show function should print Project deleted successfully when project deleted`() {
        // Given
        every { inputReader.readInput(any()) } returns "1" andThen "yes"
        coEvery { getProjectByIdUseCase.invoke("1") } returns mockProject
        coEvery { deleteUseCase.deleteProject("1") } returns mockk()

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Project deleted successfully.") }
    }

    @Test
    fun `show function should print Deletion cancelled when cancel project deletion`() {
        // Given
        every { inputReader.readInput(any()) } returns "2" andThen "no"
        coEvery { getProjectByIdUseCase.invoke("2") } returns mockProject

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Deletion cancelled.") }
        coVerify(exactly = 0) { deleteUseCase.deleteProject(any()) }
    }

    @Test
    fun `show function should print Failed to delete project Not found when can't delete project`() {
        // Given
        every { inputReader.readInput(any()) } returns "3" andThen "yes"
        coEvery { getProjectByIdUseCase.invoke("3") } returns mockProject
        coEvery { deleteUseCase.deleteProject("3") } throws Exception("Not found")

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Failed to delete project: Not found") }
    }

    @Test
    fun `show function should print Project not found when project not found`() {
        // Given
        every { inputReader.readInput(any()) } returns "4"
        coEvery { getProjectByIdUseCase.invoke("4") } throws ProjectExceptions.ProjectNotFoundException()

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Project not found") }
        coVerify(exactly = 0) { deleteUseCase.deleteProject(any()) }
    }
}