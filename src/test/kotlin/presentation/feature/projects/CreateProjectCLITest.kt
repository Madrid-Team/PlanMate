package presentation.feature.projects

import domain.usecases.project.CreateProjectUseCase
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter

class CreateProjectCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val useCase = mockk<CreateProjectUseCase>()

    private lateinit var cli: CreateProjectCLI

    @BeforeEach
    fun setUp() {
        cli = CreateProjectCLI(inputReader, outputPrinter, useCase, mockk())
    }


    @Test
    fun `should create project successfully when create project then return success`() {
        every { inputReader.readInput(any()) } returnsMany listOf("project name", "description")

        val project = helperProject(id = "1", name = "project name", description = "description")
        every { useCase.createProject(project) } returns Result.success(Unit)

        cli.show()

        verify { outputPrinter.printMessage("Project created successfully") }
    }

    @Test
    fun `should show error message when creation fails`() {
        every { inputReader.readInput(any()) } returnsMany listOf("project name", "description")

        val project = helperProject(id = "1", name = "project name", description = "description")
        every { useCase.createProject(project) } returns Result.failure(PlanMateExceptions("failed"))

        cli.show()

        verify { outputPrinter.printMessage("Failed to create project") }
    }
}