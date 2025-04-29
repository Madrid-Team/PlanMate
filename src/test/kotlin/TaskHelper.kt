import domain.models.logs.AuditLog
import domain.models.task.Task
import domain.models.task.TaskState
import java.util.*

fun createTask(
    id: String = UUID.randomUUID().toString(),
    projectId: String = "",
    title: String = "",
    description: String = "",
    state: TaskState = TaskState.TODO,
    createdBy: String = "",
    logs: List<AuditLog> = listOf(),
) = Task(
    id = id,
    projectId = projectId,
    title = title,
    description = description,
    state = state,
    createdBy = createdBy,
    logs = logs
)