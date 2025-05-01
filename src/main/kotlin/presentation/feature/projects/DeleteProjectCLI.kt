package presentation.feature.projects

import domain.usecases.project.DeleteProjectUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteProjectUseCase: DeleteProjectUseCase
) {
    fun show() {
        outputPrinter.printMessage("=== Delete Project ===")
        val projectId = inputReader.readInput("Enter project ID to delete: ")
        val confirmed = inputReader.readInput("Are you sure you want to delete this project? (yes/no): ")

        when (confirmed.lowercase()) {
            "yes" -> {
                val result = deleteProjectUseCase.deleteProject(projectId)
                result.onSuccess {
                    outputPrinter.printMessage("Project deleted successfully.")
                }.onFailure {
                    outputPrinter.printMessage("Failed to delete project: ${it.message}")
                }
            }

            else -> {
                outputPrinter.printMessage("Deletion cancelled.")

            }

        }
    }
}