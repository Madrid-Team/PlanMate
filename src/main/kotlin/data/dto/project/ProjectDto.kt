package data.dto.project

import data.dto.task.TaskDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*


@Serializable
data class ProjectDto(
    @BsonId @SerialName("_id") val id: String,
    val name: String,
    val description: String,
    val createdBy: String,
    val projectLogs: List<String>,
    val projectState: String,
    val taskStates: List<String>,
    val projectStates: List<String>,
    val matesIds:List<String> = emptyList(),
    val tasks:List<TaskDto> = emptyList()
)