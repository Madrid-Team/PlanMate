package presentation.feature.projects

import domain.usecases.project.GetProjectLogsByIdUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class ProjectAuditLogCLI(
    private val reader: InputReader,
    private val printer: OutputPrinter,
    private val getProjectLogsByIdUseCase: GetProjectLogsByIdUseCase
) {
    fun show() {
        printer.printMessage("=== Project Audit Log ===")
        val projectId = reader.readInput("Enter Project ID to view audit logs: ")

        getProjectLogsByIdUseCase.getProjectLogsById(projectId)
            .onSuccess { logs ->
                if (logs.isEmpty()) {
                    printer.printMessage("No audit logs found for project ID: $projectId\n")
                } else {
                    printer.printMessage("Audit logs for project ID: $projectId\n")
                    logs.forEach { log ->
                        printer.printMessage("- $log\n")
                    }
                }
            }
            .onFailure { error ->
                printer.printError("Failed to fetch audit logs: ${error.message}\n")
            }
    }
}
