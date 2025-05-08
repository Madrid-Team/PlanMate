package presentation

import com.google.common.truth.Truth.assertThat
import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.models.logs.CurrentUser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.AuthenticationCLI
import presentation.feature.admin.AdminCLI
import presentation.feature.projects.ProjectAuditLogCLI
import presentation.feature.projects.ProjectCLI
import presentation.feature.tasks.TaskCLI
import presentation.feature.user.UserCLI
import java.util.*

class PlanMateCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var authenticationCLI: AuthenticationCLI
    private lateinit var taskCLI: TaskCLI
    private lateinit var projectCLI: ProjectCLI
    private lateinit var userCLI: UserCLI
    private lateinit var adminCLI: AdminCLI
    private lateinit var projectAuditLogCLI: ProjectAuditLogCLI
    private lateinit var planMateCLI: PlanMateCLI


    @BeforeEach
    fun setUp() {
        inputReader = mockk(relaxed = true)
        outputPrinter = mockk(relaxed = true)
        authenticationCLI = mockk(relaxed = true)
        taskCLI = mockk(relaxed = true)
        projectCLI = mockk(relaxed = true)
        userCLI = mockk(relaxed = true)
        adminCLI = mockk(relaxed = true)
        projectAuditLogCLI = mockk(relaxed = true)
        planMateCLI = PlanMateCLI(inputReader, outputPrinter, authenticationCLI, taskCLI, projectCLI, userCLI, adminCLI)
        mockkObject(CurrentUser)
        mockkStatic(UUID::class)
    }

    @Test
    fun `should print welcome  and main menu and exit message when app starts when select 0`() {
        runTest {
            // Given
            every { inputReader.readInput("Select an option: ") } returns "0"
            every { CurrentUser.getCurrentUser() } returns null
            // when
            planMateCLI.start()

            // then
            verify {
                outputPrinter.printMessage("=== Welcome to PlanMate ===")
                outputPrinter.printMessage("=== === Main Menu === ===")
                outputPrinter.printMessage("1. Log in")
                outputPrinter.printMessage("0. Exit")
                outputPrinter.printMessage("Goodbye!")
            }
        }
    }

    @Test
    fun `should print invalid option message when app starts when select invalid option`() {
        runTest {
            // Given
            every { inputReader.readInput("Select an option: ") } returns "z" andThen "0"
            every { CurrentUser.getCurrentUser() } returns null
            // when
            planMateCLI.start()

            // then
            verify {
                outputPrinter.printMessage("=== Welcome to PlanMate ===")
                outputPrinter.printMessage("=== === Main Menu === ===")
                outputPrinter.printMessage("1. Log in")
                outputPrinter.printMessage("0. Exit")
                outputPrinter.printError("Invalid option.")
                outputPrinter.printMessage("Goodbye!")
            }
        }
    }

    @Test
    fun `should call authenticationCLI login when user selects 1`() {
        runTest {
            every { inputReader.readInput(any()) } returnsMany listOf("1")
            every { CurrentUser.getCurrentUser() } returns null

            planMateCLI.start()

            verify { authenticationCLI.login() }
        }
    }

    @Test
    fun `should show admin menu when admin login success`() {
        runTest {
            // Given
            val username = "admin"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "ADMIN")
            every { CurrentUser.getCurrentUser() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "0")
            // When
            planMateCLI.start()


            // Then
            verify {
                outputPrinter.printMessage("=== Welcome to PlanMate ===")
                outputPrinter.printMessage("=== === Main Menu === ===")
                outputPrinter.printMessage("1. Log in")
                outputPrinter.printMessage("0. Exit")
                assertThat(user).isNotNull()
                outputPrinter.printMessage("\nWelcome, ${user.username}! (Role: ${user.role})")
                outputPrinter.printMessage("=== Admin Menu ===")
                outputPrinter.printMessage("1. Manage tasks")
                outputPrinter.printMessage("2. Manage projects")
                outputPrinter.printMessage("3. Manage users")
                outputPrinter.printMessage("4. Admin tools")
                outputPrinter.printMessage("0. Log out")
            }
        }
    }

    @Test
    fun `should show mate menu when mate login success`() {
        runTest {
            // Given
            val username = "mate"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "MATE")
            every { CurrentUser.getCurrentUser() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "0")
            // When
            planMateCLI.start()


            // Then
            verify {
                outputPrinter.printMessage("=== Welcome to PlanMate ===")
                outputPrinter.printMessage("=== === Main Menu === ===")
                outputPrinter.printMessage("1. Log in")
                outputPrinter.printMessage("0. Exit")
                assertThat(user).isNotNull()
                outputPrinter.printMessage("\nWelcome, ${user.username}! (Role: ${user.role})")
                outputPrinter.printMessage("=== Mate Menu ===")
                outputPrinter.printMessage("1. View my tasks")
                outputPrinter.printMessage("2. View projects")
                outputPrinter.printMessage("0. Log out")
            }
        }
    }

    @Test
    fun `should show taskCLI when user selects 1 in admin menu`() {
        runTest {
            // given
            val username = "admin"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "ADMIN")
            every { CurrentUser.getCurrentUser() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "1", "0")

            // when
            planMateCLI.start()

            // then
            coVerify { taskCLI.show() }
        }
    }

    @Test
    fun `should show projectCLI when user selects 2 in admin menu`() {
        runTest {
            // given
            val username = "admin"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "ADMIN")
            every { CurrentUser.getCurrentUser() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "2", "0")

            // when
            planMateCLI.start()

            // then
            coVerify { projectCLI.show() }
        }
    }

    @Test
    fun `should show userCLI when user selects 3 in admin menu`() {
        runTest {
            // given
            val username = "admin"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "ADMIN")
            every { CurrentUser.getCurrentUser() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "3", "0")

            // when
            planMateCLI.start()

            // then
            verify { userCLI.show() }
        }
    }

    @Test
    fun `should show adminCLI when user selects 4 in admin menu`() {
        runTest {
            // given
            val username = "admin"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "ADMIN")
            every { CurrentUser.getCurrentUser() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "4", "0")

            // when
            planMateCLI.start()

            // then
            coVerify { adminCLI.showAdminMenu() }
        }
    }

    @Test
    fun `should show taskCLI when user selects 1 in mate menu`() {
        runTest {
            // given
            val username = "mate"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "MATE")
            every { CurrentUser.getCurrentUser() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "1", "0")

            // when
            planMateCLI.start()

            // then
            coVerify { taskCLI.show() }
        }
    }

    @Test
    fun `should show taskCLI when user selects 2 in mate menu`() {
        runTest {
            // given
            val username = "mate"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "MATE")
            every { CurrentUser.getCurrentUser() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "2", "0")

            // when
            planMateCLI.start()

            // then
            coVerify { projectCLI.showProjects() }
        }
    }
}
