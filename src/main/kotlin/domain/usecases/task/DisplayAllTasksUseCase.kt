package domain.usecases.task

import domain.repository.ProjectRepository
import domain.repository.TaskRepository

class DisplayAllTasksUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    fun display(projectId: String): Result<String> {
        return try {
            val project = projectRepository.getProjectById(projectId)
                ?: return Result.failure(Exception("Project not found."))

            val tasks = taskRepository.getTasksByProjectId(projectId)
            val list = tasks.getOrNull()
            val states = project.taskStates
            val swimlanesMap = states.associateWith { mutableListOf<String>() }

            if (list != null) {
                for (task in list) {
                    swimlanesMap[task.taskState]?.add(task.title)
                }
            }

            val formatted = formatAsSwimlanes(states, swimlanesMap)
            Result.success(formatted)
        } catch (e: Exception) {
            Result.failure(e)
        }
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