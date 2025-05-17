package presentation.feature.projects

import data.source.csv.user.CurrentUserProvider
import domain.models.logs.AuditLog
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.usecases.logs.AddAuditLogUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.ProjectExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class DeleteProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val addAuditLogUseCase: AddAuditLogUseCase,
    private val currentUserProvider: CurrentUserProvider
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.deleteProjectHeader)
        val projectId = inputReader.readInput(String.enterProjectIdToDelete)
        val project = getProjectByIdUseCase.getProjectById(projectId)
        try {
            when (inputReader.readInput(String.sureYouWantToDeleteThisProject)) {
                String.selectionOne -> {
                    deleteProjectUseCase.deleteProjectByProjectId(projectId)
                    addAuditLogUseCase.addAuditLog(
                        AuditLog(
                            operationType = OperationType.DELETE,
                            entityName = project.name,
                            entityType = EntityType.PROJECT,
                            entityId = project.id.toString(),
                            username = currentUserProvider.getCurrentUser().username,
                        )
                    )
                    outputPrinter.printMessage(String.deleteProjectSuccess)
                }

                else -> {
                    outputPrinter.printMessage(String.deletionCancelled)
                }
            }
        } catch (exception: ProjectExceptions) {
            outputPrinter.printMessage(String.deleteProjectException.format(exception.message))
        }
    }
}