package data.utils

import domain.utils.*
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
        is FileNotFoundException -> ProjectsFileNotExistsException()
        is IOException -> ProjectsReadWriteException()
        is ProjectExceptions -> exception
        is PlanMateExceptions -> exception
        else -> {
            PlanMateExceptions(exception?.message.toString())
        }
    }
}


fun Throwable?.toTaskException(): PlanMateExceptions {
    return when (val exception = this) {
        is FileNotFoundException -> TaskNotFoundException()
        is IOException -> TaskNotFoundException()
        is TaskExceptions -> exception
        is PlanMateExceptions -> exception
        else -> {
            PlanMateExceptions(exception?.message.toString())
        }
    }
}


fun Throwable.toUserException(): PlanMateExceptions {
    return when (val exception = this) {
        is FileNotFoundException -> UserNotFoundException()
        is IOException -> UserReadWriteException()
        is UserExceptions -> exception
        is PlanMateExceptions -> exception
        else -> {
            PlanMateExceptions(exception.message.toString())
        }
    }
}

