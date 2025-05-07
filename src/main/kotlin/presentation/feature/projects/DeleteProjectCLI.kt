package presentation.feature.projects

import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utlis.ProjectExceptions
import kotlinx.coroutines.runBlocking
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

        try {
            val confirmed = inputReader.readInput("Are you sure you want to delete this project? (yes/no): ")
            when (confirmed.lowercase()) {
                "yes" -> {
                    runBlocking {
                        deleteProjectUseCase.deleteProject(projectId)
                    }
                    outputPrinter.printMessage("Project deleted successfully.")
                }

                else -> {
                    outputPrinter.printMessage("Deletion cancelled.")

                }

            }
        } catch (e: ProjectExceptions.ProjectNotFoundException) {
            outputPrinter.printMessage(e.message ?: "Project not found")
        } catch (e: Exception) {
            outputPrinter.printMessage("Failed to delete project: ${e.message}")
        }
    }
}