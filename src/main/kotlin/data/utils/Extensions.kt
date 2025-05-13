package data.utils

import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions
import domain.utils.TaskExceptions
import domain.utils.UserExceptions
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


val String.Companion.project: String get() = "project.csv"
val String.Companion.task: String get() = "task.csv"
val String.Companion.user: String get() = "user.csv"

val String.Companion.projectHeader: String get() = "id,name,description,createdBy,projectLogs,projectState,taskStates,projectStates\n"
val String.Companion.taskHeader: String get() = "id,projectId,title,description,state,createdBy,logs\n"
val String.Companion.userHeader: String get() = "id,userName,passwordHash,userRole\n1,Mohamed Ashraf,12345678,ADMIN\n"


fun String.appendHeader(file: File) {
    when (this) {
        String.project -> file.appendText(String.projectHeader)
        String.task -> file.appendText(String.taskHeader)
        String.user -> file.appendText(String.userHeader)
    }
}

fun Throwable?.toProjectException(): PlanMateExceptions {
    return when (val exception = this) {
        is FileNotFoundException -> ProjectExceptions.ProjectsFileNotExistsException()
        is IOException -> ProjectExceptions.ProjectsReadWriteException()
        is ProjectExceptions -> exception
        is PlanMateExceptions -> exception
        else -> {
            PlanMateExceptions(exception?.message.toString())
        }
    }
}


fun Throwable?.toTaskException(): PlanMateExceptions {
    return when (val exception = this) {
        is FileNotFoundException -> TaskExceptions.TaskNotFoundException()
        is IOException -> TaskExceptions.TaskNotFoundException()
        is TaskExceptions -> exception
        is PlanMateExceptions -> exception
        else -> {
            PlanMateExceptions(exception?.message.toString())
        }
    }
}


fun Throwable.toUserException(): PlanMateExceptions {
    return when (val exception = this) {
        is FileNotFoundException -> UserExceptions.UserNotFoundException()
        is IOException -> UserExceptions.UserReadWriteException()
        is UserExceptions -> exception
        is PlanMateExceptions -> exception
        else -> {
            PlanMateExceptions(exception.message.toString())
        }
    }
}

