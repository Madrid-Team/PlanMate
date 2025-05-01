package domain.usecases

import domain.repository.ProjectRepository
import domain.repository.TaskRepository

class DisplayAllTasksUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    fun display(projectId: String): String {
        val project = projectRepository.getProjectById(projectId)
            ?: return "Project not found."
        val tasks = taskRepository.getTasksByProjectId(projectId)
        val states = project.taskStates
        val swimlanesMap = states.associateWith { mutableListOf<String>() }

        for (task in tasks) {
            swimlanesMap[task.state]?.add(task.title)
        }

        return formatAsSwimlanes(states, swimlanesMap)
    }
    private fun formatAsSwimlanes(states: List<String>, swimlanesMap: Map<String, List<String>>): String {
        val builder = StringBuilder()

        for (state in states) {
            builder.append("$state:\n")
            val tasks = swimlanesMap[state].orEmpty()
            if (tasks.isEmpty()) {
                builder.append("\n")
            } else {
                for (task in tasks) {
                    builder.append("- $task\n")
                }
                builder.append("\n")
            }
        }

        return builder.toString().trimEnd()
    }


}