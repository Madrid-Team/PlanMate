package data.dto.task


data class TaskDto(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val taskState: String,
    val createdBy: String,
    val logs: List<String>,
)