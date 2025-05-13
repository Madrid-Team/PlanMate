package presentation.feature.projects

import domain.models.project.Project
import domain.usecases.project.GetAllProjectsUseCase
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*
import java.util.*


class ProjectCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var createProjectCLI: CreateProjectCLI
    private lateinit var deleteProjectCLI: DeleteProjectCLI
    private lateinit var editProjectCLI: EditProjectCLI
    private lateinit var projectAuditLogCLI: ProjectAuditLogCLI
    private lateinit var projectView: ProjectView
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var projectCLI: ProjectCLI

    val project1 = Project(
        id = UUID.randomUUID(),
        name = "name",
        description = "dec",
        createdBy = "createdBy",
        projectLogs = listOf(),
        projectState = "state",
        taskStates = listOf(),
        projectStates = listOf(),
    )
    val project2 = Project(
        id = UUID.randomUUID(),
        name = "name",
        description = "dec",
        createdBy = "createdBy",
        projectLogs = listOf(),
        projectState = "state",
        taskStates = listOf(),
        projectStates = listOf(),
    )
    val testProjects = listOf(project1, project2)


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
    fun `should navigate to CreateProjectCLI when user selects 2`() {
        runTest {
            // given
            every { inputReader.readInput(any()) } returnsMany listOf("2", "0")

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
            every { inputReader.readInput(any()) } returnsMany listOf("3", "0")

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
            every { inputReader.readInput(any()) } returnsMany listOf("4", "0")

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
            every { inputReader.readInput(any()) } returnsMany listOf("5", "0")

            // when
            projectCLI.show()

            // then
            coVerify { projectAuditLogCLI.show() }
        }
    }

    @Test
    fun `should show only project menu when select zero`() {
        runTest {
            // given
            every { inputReader.readInput(any()) } returnsMany listOf("0")

            // when
            projectCLI.show()

            // then
            verify {
                outputPrinter.printMenuItems(
                    listOf(
                        String.projectMenuHeader,
                        String.displayProjects,
                        String.createProject,
                        String.editProject,
                        String.deleteProject,
                        String.displayProjectLogs,
                        String.back
                    )
                )
            }
            coVerify(exactly = 0) {
                createProjectCLI.show()
                editProjectCLI.show()
                deleteProjectCLI.show()
                projectAuditLogCLI.show()
            }
        }
    }

    @Test
    fun `should print invalid option message when user selects unknown option`() {
        runTest {
            // given
            every { inputReader.readInput(any()) } returnsMany listOf("9", "0")

            // when
            projectCLI.show()

            // then
            verify { outputPrinter.printError(String.invalidOption) }
        }
    }

    @Test
    fun `showProjects should display projects when success`() {
        runTest {
            coEvery { getAllProjectsUseCase.getProjects() } returns testProjects

            projectCLI.showProjects()

            coVerify {
                outputPrinter.printMessage(String.displayProjects)
                getAllProjectsUseCase.getProjects()
                projectView.projectList(testProjects)
            }
        }
    }

    @Test
    fun `showProjects should throw ProjectExceptions`() {
        runTest {
            coEvery { getAllProjectsUseCase.getProjects() } throws ProjectExceptions("")

            projectCLI.showProjects()

            coVerify {
                outputPrinter.printError("${String.failedLoadProjects} ")
            }
        }
    }

}