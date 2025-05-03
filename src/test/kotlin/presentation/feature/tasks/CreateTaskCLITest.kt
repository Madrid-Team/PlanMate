package presentation.feature.tasks

import domain.usecases.task.CreateTaskUseCase
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
        // Given
        every { inputReader.readInput(any()) } returnsMany listOf("1", "title", "description")
        val project = helperProject(id = "1")
        val task = helperTask(projectId = project.id.toString(), title = "title", description = "description")
        every { useCase.createTask(task) } returns true

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Task created successfully") }
    }

    @Test
    fun `should show error message when project ID is not found and not create task`() {
        // Given
        every { inputReader.readInput(any()) } returnsMany listOf("invalid_id", "title", "description")
        val task = helperTask(projectId = "invalid_id", title = "title", description = "description")
        every { useCase.createTask(task) } returns false

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage("Failed to create task") }
    }
}