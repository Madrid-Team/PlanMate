package presentation

import presentation.feature.tasks.TaskCLI
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.AuthenticationCLI
import presentation.feature.ProjectAuditLogCLI
import presentation.feature.TaskAuditLogCLI
import presentation.feature.projects.ProjectCLI
import presentation.feature.user.UserCLI

class PlanMateCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val authenticationCLI: AuthenticationCLI,
    private val taskCLI: TaskCLI,
    private val projectCLI: ProjectCLI,
    private val userCLI: UserCLI,
    private val auditLogCLI: TaskAuditLogCLI,
    private val projectAuditLogCLI: ProjectAuditLogCLI
) {
    fun start() {
        outputPrinter.printMessage("Welcome to PlanMate!")
    }
}