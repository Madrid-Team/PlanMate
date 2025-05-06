package presentation.feature.projects

import data.utils.toProjectException
import domain.usecases.project.GetProjectLogsByIdUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class ProjectAuditLogCLITest {

    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter
    private lateinit var useCase: GetProjectLogsByIdUseCase
    private lateinit var cli: ProjectAuditLogCLI

    @BeforeEach
    fun setup() {
        reader = mockk()
        printer = mockk(relaxed = true)
        useCase = mockk()
        cli = ProjectAuditLogCLI(reader, printer, useCase)
    }

    @Test
    fun `show should print audit logs when logs are available`() {
        val projectId = "123"
        val logs = listOf("Created", "Updated", "Deleted")

        every { reader.readInput(any()) } returns projectId
        every { useCase.getProjectLogsById(projectId) } returns logs

        cli.show()

        verifySequence {
            printer.printMessage("=== Project Audit Log ===")
            reader.readInput("Enter Project ID to view audit logs: ")
            printer.printMessage("Audit logs for project ID: $projectId\n")
            printer.printMessage("- Created\n")
            printer.printMessage("- Updated\n")
            printer.printMessage("- Deleted\n")
        }
    }

    @Test
    fun `show should print message when no logs are found`() {
        val projectId = "456"

        every { reader.readInput(any()) } returns projectId
        every { useCase.getProjectLogsById(projectId) } returns emptyList()

        cli.show()

        verifySequence {
            printer.printMessage("=== Project Audit Log ===")
            reader.readInput("Enter Project ID to view audit logs: ")
            printer.printMessage("No audit logs found for project ID: $projectId\n")
        }
    }

    @Test
    fun `show should print error when use case fails`() {
        val projectId = "789"
        val exception = RuntimeException("Something went wrong")

        every { reader.readInput(any()) } returns projectId
        every { useCase.getProjectLogsById(projectId) } throws exception.toProjectException()

        cli.show()

        verifySequence {
            printer.printMessage("=== Project Audit Log ===")
            reader.readInput("Enter Project ID to view audit logs: ")
            printer.printError("Failed to fetch audit logs: Something went wrong\n")
        }
    }
}