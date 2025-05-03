package presentation

import com.google.common.truth.Truth.assertThat
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.models.logs.CurrentUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.AuthenticationCLI
import presentation.feature.projects.ProjectAuditLogCLI
import presentation.feature.admin.AdminCLI
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
        // when
        planMateCLI.start()

        // then
        val printed = outputStream.toString().trim()
        assertThat(printed).contains("Welcome to PlanMate!")
    }

    @Test
    fun `should call authenticationCLI login when user selects login`() {
        every { inputReader.readInput(any()) } returnsMany listOf("1", "0")
        every { CurrentUser.getCurrentUser() } returns null

        planMateCLI.start()

        verify { authenticationCLI.login() }
    }


    @Test
    fun `should show task menu when admin selects manage tasks`() {
        val adminUser = User(id =  UUID.fromString("1"), username = "admin", role = UserRole.ADMIN.name, passwordHash = "")
        every { CurrentUser.getCurrentUser() } returns adminUser
        every { inputReader.readInput(any()) } returnsMany listOf("1", "0")

        planMateCLI.start()

        verify { taskCLI.show() }
    }

    @Test
    fun `should show project menu when member selects view projects`() {
        val memberUser = User(id = UUID.fromString("2"), username = "user", role = UserRole.ADMIN.name, passwordHash = "")
        every { CurrentUser.getCurrentUser() } returns memberUser
        every { inputReader.readInput(any()) } returnsMany listOf("2", "0")

        planMateCLI.start()

        verify { projectCLI.show() }
    }

    @Test
    fun `should print invalid option when user enters wrong input`() {
        every { inputReader.readInput(any()) } returnsMany listOf("9", "0")
        every { CurrentUser.getCurrentUser() } returns null

        planMateCLI.start()

        verify { outputPrinter.printMessage("Invalid option") }
    }
}