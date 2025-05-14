package presentation.feature.projects

import domain.models.project.Project
import presentation.components.OutputPrinter
import presentation.utils.*

class ProjectView(
    private val outputPrinter: OutputPrinter
) {
    fun projectList(projects: List<Project>) {


        outputPrinter.printMessage(String.projectsList)

        // Data Rows
        projects.forEach { project ->
            outputPrinter.printMessage(formatProjectDetails(project))
            outputPrinter.printMessage(String.projectSeparator)
        }

    }

    private fun formatProjectDetails(project: Project): String {
        return listOf(
            "${String.projectId} : ${project.id}",
            "${String.projectName} : ${project.name}",
            "${String.projectDescription} : ${project.description}",
            "${String.projectState} : ${project.projectState}",
            "${String.createdBy} : ${project.createdBy}",
            "${String.projectLogs} :\n${project.projectLogs.joinToString("\n") { "     -- $it" }}",
            "${String.projectStates} : ${project.projectStates.joinToString(", ")}",
            "${String.taskStates} : ${project.taskStates.joinToString(", ")}"
        ).joinToString("\n")
    }
}