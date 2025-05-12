package presentation.feature.projects

import domain.usecases.project.GetProjectLogsByIdUseCase
import domain.utils.PlanMateExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.auditLogException
import presentation.utils.auditLogsForProjectId
import presentation.utils.enterProjectIDToViewAudit
import presentation.utils.projectAuditLogHeader

class ProjectAuditLogCLI(
    private val reader: InputReader,
    private val printer: OutputPrinter,
    private val getProjectLogsByIdUseCase: GetProjectLogsByIdUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        printer.printMessage(String.projectAuditLogHeader)
        val projectId = reader.readInput(String.enterProjectIDToViewAudit)
        try {
            val logs = getProjectLogsByIdUseCase.execute(projectId)
            printer.printMessage(String.auditLogsForProjectId.format(projectId))
            logs.forEach { log ->
                printer.printMessage("- $log\n")
            }
        } catch (e: PlanMateExceptions) {
            printer.printError(String.auditLogException.format(e.message))
        }
    }
}
