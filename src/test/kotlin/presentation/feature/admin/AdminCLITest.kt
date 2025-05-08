package presentation.feature.admin

import org.junit.jupiter.api.*
import java.io.*
import kotlin.test.assertTrue
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.madrid.presentation.feature.tasks.TaskAuditLogCLI
import presentation.feature.projects.ProjectAuditLogCLI

class AdminCLITest {

    private lateinit var inputStream: ByteArrayInputStream
    private lateinit var outputStream: ByteArrayOutputStream
    private lateinit var printStream: PrintStream

    private lateinit var projectAuditLogCLI: ProjectAuditLogCLI
    private lateinit var taskAuditLogCLI: TaskAuditLogCLI

    @BeforeEach
    fun setup() {
        projectAuditLogCLI = mockk(relaxed = true)
        taskAuditLogCLI = mockk(relaxed = true)

        outputStream = ByteArrayOutputStream()
        printStream = PrintStream(outputStream)
        System.setOut(printStream)
    }

    private fun provideInput(vararg lines: String) {
        val input = lines.joinToString("\n")
        inputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(inputStream)
    }

    @Test
    fun `should call projectAuditLogCLI when user selects 1`() {
        runTest {
            provideInput("1", "0")

            val adminCLI = AdminCLI(projectAuditLogCLI, taskAuditLogCLI)
            adminCLI.showAdminMenu()

            coVerify(exactly = 1) { projectAuditLogCLI.show() }
        }
    }

    @Test
    fun `should call taskAuditLogCLI when user selects 2`() {
        runTest {
            provideInput("2", "0")

            val adminCLI = AdminCLI(projectAuditLogCLI, taskAuditLogCLI)
            adminCLI.showAdminMenu()

            coVerify(exactly = 1) { taskAuditLogCLI.show() }
        }
    }

    @Test
    fun `should print invalid option when input is invalid`() {
        runTest {
            provideInput("99", "0")

            val adminCLI = AdminCLI(projectAuditLogCLI, taskAuditLogCLI)
            adminCLI.showAdminMenu()

            val output = outputStream.toString()
            assertTrue(output.contains("Invalid option!"))
        }
    }
}
