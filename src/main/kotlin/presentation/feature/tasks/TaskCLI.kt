package presentation.feature.tasks

class TaskCLI(
    private val createTaskCLI: CreateTaskCLI,
    private val editTaskCLI: EditTaskCLI,
    private val deleteTaskCLI: DeleteTaskCLI,
    private val taskView: TaskView
) {
    fun show() {
    }
}
