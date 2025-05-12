package presentation.feature.projects

import domain.usecases.project.DeleteProjectUseCase
import domain.utils.ProjectExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class DeleteProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteProjectUseCase: DeleteProjectUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.deleteProjectHeader)
        val projectId = inputReader.readInput(String.enterProjectIdToDelete)

        try {
            when (inputReader.readInput(String.sureYouWantToDeleteThisProject)) {
                String.selectionOne -> {
                    deleteProjectUseCase.execute(projectId)
                    outputPrinter.printMessage(String.deleteProjectSuccess)
                }

                else -> {
                    outputPrinter.printMessage(String.deletionCancelled)
                }
            }
        } catch (exception: ProjectExceptions.ProjectNotFoundException) {
            outputPrinter.printMessage(String.deleteProjectNotFoundException.format(exception.message))
        } catch (exception: Exception) {
            outputPrinter.printMessage(String.deleteProjectException.format(exception.message))
        }
    }
}