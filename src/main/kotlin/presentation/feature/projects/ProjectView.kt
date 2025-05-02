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

// Data Rows
        projects.forEach { project ->
            outputPrinter.printMessage(
                "ID : ${project.id}\n" +
                        "Name : ${project.name}\n" +
                        "Description : ${project.description}\n" +
                        "Project State : ${project.projectState}\n" +
                        "Created By : ${project.createdBy}\n" +
                        "Project Logs : ${project.projectLogs}\n" +
                        "Project States : ${project.projectStates.joinToString(", ")}\n" +
                        "Task States : ${project.taskStates.joinToString(", ")}\n"
            )

            outputPrinter.printMessage("+--------------------------------------+")
        }

    }
}