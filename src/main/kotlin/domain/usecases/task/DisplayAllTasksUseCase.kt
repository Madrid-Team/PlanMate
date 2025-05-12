package domain.usecases.task

import domain.repository.ProjectRepository
import domain.repository.TaskRepository

class DisplayAllTasksUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    suspend fun display(projectId: String): String {
        val project = projectRepository.getProjectById(projectId)
        val tasks = taskRepository.getTasksByProjectId(projectId)
        val states = project.taskStates
        val swimlanesMap = states.associateWith { mutableListOf<String>() }

        for (task in tasks) {
            swimlanesMap[task.taskState]?.add(task.title)
        }
        return formatAsSwimlanes(states, swimlanesMap)
    }

    private fun formatAsSwimlanes(states: List<String>, swimlanesMap: Map<String, List<String>>): String {
        val builder = StringBuilder()

        for (state in states) {
            val tasks = swimlanesMap[state].orEmpty()
            if (tasks.isEmpty()) {
                builder.append("\n")
            } else {
                builder.append("$state:\n")
                for (task in tasks) {
                    builder.append("  - $task\n")
                }
            }
            builder.append("\n")

        }
        return builder.toString().trimEnd()
    }
}