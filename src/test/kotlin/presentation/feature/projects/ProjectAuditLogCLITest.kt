package presentation.feature.projects

import domain.usecases.project.GetProjectLogsByIdUseCase
import domain.utils.ProjectExceptions
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.auditLogException
import presentation.utils.auditLogsForProjectId
import presentation.utils.enterProjectIDToViewAudit
import presentation.utils.projectAuditLogHeader
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
    fun `show should print audit logs when logs are available`() = runTest {
        val projectId = "123"
        val logs = listOf("Created", "Updated", "Deleted")

        every { reader.readInput(String.enterProjectIDToViewAudit) } returns projectId
        coEvery { useCase.getProjectLogsByProjectId(projectId) } returns logs

        cli.show()

        verifySequence {
            printer.printMessage(String.projectAuditLogHeader)
            reader.readInput(String.enterProjectIDToViewAudit)
            printer.printMessage(String.auditLogsForProjectId.format(projectId))
            printer.printMessage("- Created\n")
            printer.printMessage("- Updated\n")
            printer.printMessage("- Deleted\n")
        }
    }

    @Test
    fun `show should print message when no logs are found`() = runTest {
        val projectId = "456"
        val exception = ProjectExceptions.NoLogsFoundException("There is no logs for this project")

        every { reader.readInput(String.enterProjectIDToViewAudit) } returns projectId
        coEvery { useCase.getProjectLogsByProjectId(projectId) } throws exception

        cli.show()

        verifySequence {
            printer.printMessage(String.projectAuditLogHeader)
            reader.readInput(String.enterProjectIDToViewAudit)
            printer.printError(String.auditLogException.format(exception.message))
        }
    }



    @Test
    fun `show should print error when use case fails`() = runTest {
        val projectId = "789"
        val exceptionMessage = "Something went wrong"
        val exception = ProjectExceptions(exceptionMessage)

        every { reader.readInput(String.enterProjectIDToViewAudit) } returns projectId
        coEvery { useCase.getProjectLogsByProjectId(projectId) } throws exception

        cli.show()

        verifySequence {
            printer.printMessage(String.projectAuditLogHeader)
            reader.readInput(String.enterProjectIDToViewAudit)
            printer.printError(String.auditLogException.format(exceptionMessage))
        }
    }
}
