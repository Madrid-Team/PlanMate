package presentation.feature.projects

import domain.usecases.project.GetProjectLogsByIdUseCase
import domain.utils.PlanMateExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter

class ProjectAuditLogCLI(
    private val reader: InputReader,
    private val printer: OutputPrinter,
    private val getProjectLogsByIdUseCase: GetProjectLogsByIdUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        printer.printMessage("=== Project Audit Log ===")
        val projectId = reader.readInput("Enter Project ID to view audit logs: ")
        try {
            val logs = getProjectLogsByIdUseCase(projectId)
            if (logs.isEmpty()) {
                printer.printMessage("No audit logs found for project ID: $projectId\n")
            } else {
                printer.printMessage("Audit logs for project ID: $projectId\n")
                logs.forEach { log ->
                    printer.printMessage("- $log\n")
                }
            }
        } catch (e: PlanMateExceptions) {
            printer.printError("Failed to fetch audit logs: ${e.message}\n")
        }
    }
}
