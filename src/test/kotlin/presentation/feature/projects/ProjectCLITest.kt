package presentation.feature.projects

import domain.usecases.project.GetAllProjectsUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
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
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var projectView: ProjectView
    private lateinit var projectAuditLogCLI: ProjectAuditLogCLI

    @BeforeEach
    fun setup() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        createProjectCLI = mockk(relaxed = true)
        deleteProjectCLI = mockk(relaxed = true)
        editProjectCLI = mockk(relaxed = true)
        projectView = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        projectAuditLogCLI = mockk(relaxed = true)
        projectCLI = ProjectCLI(
            inputReader,
            outputPrinter,
            createProjectCLI,
            deleteProjectCLI,
            editProjectCLI,
            projectAuditLogCLI,
            projectView,
            getAllProjectsUseCase
        )
    }

    @Test
    fun `should show projects when user selects 1`() {
        runTest {
            // given
            every { inputReader.readInput("Select an option:") } returnsMany listOf("1", "0")
            coEvery { getAllProjectsUseCase.execute() } returns emptyList()

            // when
            projectCLI.show()

            // then
            verify { projectView.projectList(any()) }
        }
    }

    @Test
    fun `should navigate to CreateProjectCLI when user selects 2`() {
        runTest {
            // given
            every { inputReader.readInput("Select an option:") } returnsMany listOf("2", "0")

            // when
            projectCLI.show()

            // then
            coVerify { createProjectCLI.show() }
        }
    }

    @Test
    fun `should navigate to EditProjectCLI when user selects 3`() {
        runTest {
            // given
            every { inputReader.readInput("Select an option:") } returnsMany listOf("3", "0")

            // when
            projectCLI.show()

            // then
            coVerify { editProjectCLI.show() }
        }
    }

    @Test
    fun `should navigate to DeleteProjectCLI when user selects 4`() {
        runTest {
            // given
            every { inputReader.readInput("Select an option:") } returnsMany listOf("4", "0")

            // when
            projectCLI.show()

            // then
            coVerify { deleteProjectCLI.show() }
        }
    }

    @Test
    fun `should navigate to ProjectAuditLogCLI when user selects 5`() {
        runTest {
            // given
            every { inputReader.readInput("Select an option:") } returnsMany listOf("5", "0")

            // when
            projectCLI.show()

            // then
            coVerify { projectAuditLogCLI.show() }
        }
    }

    @Test
    fun `should print invalid option message when user selects unknown option`() {
        runTest {
            // given
            every { inputReader.readInput("Select an option:") } returnsMany listOf("9", "0")

            // when
            projectCLI.show()

            // then
            verify { outputPrinter.printMessage("Invalid option. Please try again.") }
        }
    }
}