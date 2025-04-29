package domain.usecases

import domain.models.project.Project
import java.util.*

fun createProject(
    name: String = "",
    description: String = "",
    id: String = UUID.randomUUID().toString(),
    createdBy:String = "",
    ) = Project(
        id = id,
        name = name,
        description = description,
        createdBy = createdBy,
        projectLogs = emptyList(),
    )