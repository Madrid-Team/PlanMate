package presentation.feature.projects

import domain.usecases.project.EditProjectUseCase
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class EditProjectCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var useCase: EditProjectUseCase
    private lateinit var cli: EditProjectCLI

    @BeforeEach
    fun setUp() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        cli = EditProjectCLI(inputReader, outputPrinter, useCase)
    }

    @Test
    fun `should edit project successfully when call create project`() {
        every { inputReader.readInput(any()) } returnsMany listOf("1", "project", "description")
        val project = helperProject(id = "1", name = "project", description = "description")

        every { useCase.editProject(project) } returns Result.success(Unit)

        cli.show()

        verify { outputPrinter.printMessage("Project edited successfully.") }

    }

    @Test
    fun `should show error message when editing fails`() {
        every { inputReader.readInput(any()) } returnsMany listOf("1", "project", "description")
        val project = helperProject(id = "1", name = "project", description = "description")

        every { useCase.editProject(project) } returns Result.failure(PlanMateExceptions("edit failed"))

        cli.show()

        verify { outputPrinter.printMessage("Failed to edit project: Edit failed") }
    }
}