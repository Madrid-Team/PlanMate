package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.AuditLogDto
import data.dto.authentication.UserDto
import data.dto.project.ProjectDto
import data.dto.task.TaskDto
import data.source.AuditExternalDataSource
import data.source.ProjectExternalDataSource
import data.source.TaskExternalDataSource
import data.source.UserExternalDataSource
import data.source.csv.project.*
import data.source.csv.task.*
import data.source.csv.user.*
import data.source.mongoDb.*
import data.utils.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataSourceModule = module {

    single(named("userFile")) { File("user.csv") }
    single(named("taskFile")) { File("task.csv") }
    single(named("projectFile")) { File("project.csv") }
    single(named("userValidator")) { FileValidator(get(named("userFile"))) }
    single(named("taskValidator")) { FileValidator(get(named("taskFile"))) }
    single(named("projectValidator")) { FileValidator(get(named("projectFile"))) }

    single(named("userReader")) { FileCsvReader(get(named("userValidator"))) }
    single(named("userWriter")) { FileCsvWriter(get(named("userValidator"))) }

    single(named("taskReader")) { FileCsvReader(get(named("taskValidator"))) }
    single(named("taskWriter")) { FileCsvWriter(get(named("taskValidator"))) }

    single(named("projectReader")) { FileCsvReader(get(named("projectValidator"))) }
    single(named("projectWriter")) { FileCsvWriter(get(named("projectValidator"))) }



    single { TaskCsvParser() }
    single { UserCsvParser() }
    single { ProjectCsvParser() }


    single<ProjectExternalDataSource> {
        ProjectCsvDataSource(
            get(named("projectReader")),
            get(named("projectWriter")),
            get(),
            get()
        )
    }

//    single<ExternalUserDataSource> { UserCsvDataSource(get(named("userReader")), get(named("userWriter")), get()) }
    single<TaskExternalDataSource> {
        TaskCsvDataSource(
            get(),
            get(named("taskWriter")),
            get(named("taskReader")),
            get()
        )
    }

    single { MongoClientProvider() }
    single { get<MongoClientProvider>().getDatabase() }

    single { TaskManager() }
    single { ProjectManager() }
    single<CurrentUserProvider> { UserMemoryDataSource() }

    single(named("projects")) { get<MongoDatabase>().getCollection<ProjectDto>(PROJECT_COLLECTION) }
    single(named("users")) { get<MongoDatabase>().getCollection<UserDto>(USER_COLLECTION) }
    single(named("tasks")) { get<MongoDatabase>().getCollection<TaskDto>(TASKS_COLLECTION) }
    single(named("audit_log")) { get<MongoDatabase>().getCollection<AuditLogDto>(AUDIT_LOG) }



    single<UserExternalDataSource> { UserMongoDBDataSource(get(named("users"))) }
    single<TaskExternalDataSource> { TaskMongoDBDataSource(get(named("tasks"))) }
    single<ProjectExternalDataSource> { ProjectMongoDBDataSource(get(named("projects"))) }
    single<AuditExternalDataSource> { AuditMongoDBDataSource(get(named("audit_log"))) }


}