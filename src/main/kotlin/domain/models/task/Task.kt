package domain.models.task

import java.util.*
import kotlin.collections.List


data class Task(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val title: String,
    val description: String,
    val state: String,
    val createdBy: String,
    val logs: List<String>,
)
