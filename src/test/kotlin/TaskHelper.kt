import domain.models.logs.AuditLog
import domain.models.task.Task
import java.util.*

fun createTask(
    id: String = UUID.randomUUID().toString(),
    projectId: String = "",
    title: String = "",
    description: String = "",
    state: String = "",
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