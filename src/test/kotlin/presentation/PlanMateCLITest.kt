package presentation

import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.models.logs.CurrentUser
import io.mockk.*
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
import java.io.ByteArrayOutputStream
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
    private val outputStream = ByteArrayOutputStream()
    private lateinit var user: User
    private val userMock = mockk<User>()


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
        user = mockk(relaxed = true)

        mockkObject(CurrentUser)
        every { CurrentUser.getCurrentUser() } returns userMock

        planMateCLI = PlanMateCLI(
            inputReader,
            outputPrinter,
            authenticationCLI,
            taskCLI,
            projectCLI,
            userCLI,
            adminCLI
        )

    }

    @Test
    fun `should print welcome message when app starts`() {
        every { inputReader.readInput(any()) } returns "0"

        planMateCLI.start()

        verify { outputPrinter.printMessage("=== Welcome to PlanMate ===") }
    }

    @Test
    fun `should call authenticationCLI login when user selects 1`() {
        every { inputReader.readInput(any()) } returnsMany listOf("1", "0")
        every { CurrentUser.getCurrentUser() } returns null

        planMateCLI.start()

        verify { authenticationCLI.login() }
    }

    @Test
    fun `should print invalid option when user enters wrong input`() {
        every { inputReader.readInput(any()) } returnsMany listOf("9", "0")
        every { CurrentUser.getCurrentUser() } returns null

        planMateCLI.start()

        verify { outputPrinter.printError("Invalid option.") }
    }

    @Test
    fun `should show task menu when admin selects manage tasks`() {
        val adminUser =
            User(
                id = UUID.fromString(UUID.randomUUID().toString()),
                username = "admin",
                role = UserRole.ADMIN.name,
                passwordHash = ""
            )
//        every { authenticationCLI.login() } returns Unit
        every { CurrentUser.getCurrentUser() } returns adminUser
        every { inputReader.readInput(any()) } returnsMany listOf("1", "0")

        every { taskCLI.show() } just Runs

        planMateCLI.start()

        verify { taskCLI.show() }
    }

    @Test
    fun `should show project menu when member selects view projects`() {
        val memberUser =
            User(id = UUID.fromString(UUID.randomUUID().toString()), username = "user", role = UserRole.ADMIN.name, passwordHash = "")
        every { CurrentUser.getCurrentUser() } returns memberUser
        every { inputReader.readInput(any()) } returnsMany listOf("2", "0")

        planMateCLI.start()

        verify { projectCLI.show() }
    }
}