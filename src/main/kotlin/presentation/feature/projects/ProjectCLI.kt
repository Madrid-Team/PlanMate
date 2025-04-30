package presentation.feature.projects

import presentation.components.InputReader
import presentation.components.OutputPrinter

class ProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createProjectCLI: CreateProjectCLI,
    private val deleteProjectCLI: DeleteProjectCLI,
    private val editeProjectCLI: EditProjectCLI,
) {
    fun show() {

    }
}