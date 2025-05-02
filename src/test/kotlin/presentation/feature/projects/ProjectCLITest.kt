package presentation.feature.projects

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter

class ProjectCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var createProjectCLI: CreateProjectCLI
    private lateinit var deleteProjectCLI: DeleteProjectCLI
    private lateinit var editProjectCLI: EditProjectCLI
    private lateinit var projectCLI: ProjectCLI
    private lateinit var projectView: ProjectView

    @BeforeEach
    fun setup() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        createProjectCLI = mockk(relaxed = true)
        deleteProjectCLI = mockk(relaxed = true)
        editProjectCLI = mockk(relaxed = true)
        projectView = mockk(relaxed = true)
        projectCLI = ProjectCLI(inputReader, outputPrinter, createProjectCLI, deleteProjectCLI, editProjectCLI,projectView)
    }

    @Test
    fun `should navigate to CreateProjectCLI when user selects 1`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("1", "0")

        // when
        projectCLI.show()

        // then
        verify { createProjectCLI.show() }
    }

    @Test
    fun `should navigate to EditProjectCLI when user selects 2`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("2", "0")

        // when
        projectCLI.show()

        // then
        verify { editProjectCLI.show() }
    }

    @Test
    fun `should navigate to DeleteProjectCLI when user selects 3`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("3", "0")

        // when
        projectCLI.show()

        // then
        verify { deleteProjectCLI.show() }
    }

    @Test
    fun `should print invalid option message when user selects unknown option`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("5", "0")

        // when
        projectCLI.show()

        // then
        verify { outputPrinter.printMessage("Invalid option") }
    }
}