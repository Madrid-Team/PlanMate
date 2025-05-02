package presentation.feature.projects

import domain.models.project.Project
import presentation.components.OutputPrinter

class ProjectView(private val outputPrinter: OutputPrinter) {
    fun projectList(projects: List<Project>) {
        if (projects.isEmpty()) {
            outputPrinter.printMessage("No projects to display.")
            return
        }

        outputPrinter.printMessage("=== Projects List ===")
        outputPrinter.printMessage("+------+----------------------+---------------------+")
        outputPrinter.printMessage("| ID   |         Name         |        State        |")
        outputPrinter.printMessage("+------+----------------------+---------------------+")

        projects.forEach { project ->
            outputPrinter.printMessage(
                "| ${project.id.take(4).padEnd(5)}" +
                        " | ${project.name.take(20).padEnd(20)}" +
                        " | ${project.projectState.take(19).padEnd(19)} |"
            )
        }

        outputPrinter.printMessage("+------+----------------------+---------------------+")
    }
}