package presentation.feature.tasks

import domain.usecases.DisplayAllTasksUseCase
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import org.junit.jupiter.api.Test
import io.mockk.verify
import io.mockk.every


class TaskViewTest(){
    private lateinit var displayAllTasksUseCase: DisplayAllTasksUseCase
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var taskView: TaskView

    @BeforeEach
    fun setUp() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        displayAllTasksUseCase = mockk()
        taskView = TaskView(displayAllTasksUseCase, outputPrinter, inputReader)
    }
    @Test
    fun `should print project not found when project does not exist`() {
        // Given
        val projectId = "non_existing_project"
        every { inputReader.readInput("Enter project ID: ") } returns projectId
        every { displayAllTasksUseCase.display(projectId) } returns "Project not found."

        // When
        taskView.show()

        // Then
        verify { outputPrinter.printMessage("Project not found.") }
    }

}