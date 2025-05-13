package presentation.feature.admin

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.ProjectCLI
import presentation.feature.tasks.TaskCLI
import presentation.feature.user.UserCLI
import presentation.utils.*

class AdminCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var taskCLI: TaskCLI
    private lateinit var projectCLI: ProjectCLI
    private lateinit var userCLI: UserCLI
    private lateinit var adminCLI: AdminCLI

    @BeforeEach
    fun setup() {
        taskCLI = mockk(relaxed = true)
        projectCLI = mockk(relaxed = true)
        userCLI = mockk(relaxed = true)
        inputReader = mockk(relaxed = true)
        outputPrinter = mockk(relaxed = true)
        adminCLI = AdminCLI(inputReader, outputPrinter, taskCLI, projectCLI, userCLI)
    }

    @Test
    fun `should print admin menu and logout when user select 0`() {
        runTest {
            // Given
            every { inputReader.readInput(any()) } returns "0"

            // when
            adminCLI.showAdminMenu()

            // then
            verify {
                outputPrinter.printMenuItems(
                    listOf(
                        String.adminMenu,
                        String.manageTasks,
                        String.manageProjects,
                        String.manageUsers,
                        String.logout
                    )
                )
            }
        }
    }

    @Test
    fun `should call taskCLI when user select 1`() {
        runTest {
            // Given
            every { inputReader.readInput(any()) } returnsMany listOf(
                String.selectionOne,
                String.selectionZero
            )

            // when
            adminCLI.showAdminMenu()

            // then
            coVerify {
                taskCLI.show()
            }
        }
    }

    @Test
    fun `should call projectCLI when user select 2`() {
        runTest {
            // Given
            every { inputReader.readInput(any()) } returnsMany listOf(
                String.selectionTwo,
                String.selectionZero
            )

            // when
            adminCLI.showAdminMenu()

            // then
            coVerify {
                projectCLI.show()
            }
        }
    }

    @Test
    fun `should call userCLI when user select 3`() {
        runTest {
            // Given
            every { inputReader.readInput(any()) } returnsMany listOf(
                String.selectionThree,
                String.selectionZero
            )

            // when
            adminCLI.showAdminMenu()

            // then
            coVerify {
                userCLI.show()
            }
        }
    }

    @Test
    fun `should show error message when user select invalid option`() {
        runTest {
            // Given
            every { inputReader.readInput(any()) } returnsMany listOf("a", "0")

            // when
            adminCLI.showAdminMenu()

            // then
            verify {
                outputPrinter.printError(String.invalidOption)
            }
        }
    }
}
