package presentation.feature.tasks

import domain.usecases.CreateTaskUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.helperProject

class CreateTaskCLITest() {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val useCase = mockk<CreateTaskUseCase>()
    private val taskView = mockk<TaskView>()
    private lateinit var cli: CreateTaskCLI

    @BeforeEach
    fun setUp() {
        cli = CreateTaskCLI(inputReader, outputPrinter, taskView, useCase)
    }

    @Test
    fun `should create task successfully when valid input is provided`() {
        every { inputReader.readInput(any()) } returnsMany listOf("1", "title", "description")
        val project = helperProject(id = "1")
        val task = helperTask(projectId = project.id, title = "title", description = "description")

        every { useCase.createTask(task) } returns true
        cli.show()
        verify { outputPrinter.printMessage("Task created successfully") }
    }
}