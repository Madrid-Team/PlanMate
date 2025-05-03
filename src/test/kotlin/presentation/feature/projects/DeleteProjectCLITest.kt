package presentation.feature.projects

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

    private lateinit var cli: DeleteProjectCLI

    @BeforeEach
    fun setUp() {
        cli = DeleteProjectCLI(inputReader, outputPrinter, deleteUseCase, getProjectByIdUseCase)
    }

    @Test
    fun `should delete project when success`() {
        every { inputReader.readInput(any()) } returns "1" andThen "yes"
        every { deleteUseCase.deleteProject("1") } returns Result.success(Unit)

        cli.show()

        verify { outputPrinter.printMessage("Project deleted successfully.") }
    }

    @Test
    fun `should not delete project when cancelled`() {
        every { inputReader.readInput(any()) } returns "2" andThen "no"

        cli.show()

        verify { outputPrinter.printMessage("Deletion cancelled.") }
        verify(exactly = 0) { deleteUseCase.deleteProject(any()) }
    }

    @Test
    fun `should show error message when delete fail`() {
        every { inputReader.readInput(any()) } returns "3" andThen "yes"
        every { deleteUseCase.deleteProject("3") } returns Result.failure(Exception("Not found"))

        cli.show()

        verify { outputPrinter.printMessage("Failed to delete project") }
    }
}