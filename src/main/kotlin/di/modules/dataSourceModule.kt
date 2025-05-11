package di.modules

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.authentication.UserDto
import data.dto.project.ProjectDto
import data.source.project.ProjectExternalDataSource
import data.source.project.ProjectCsvDataSource
import data.source.project.ProjectCsvParser
import data.source.project.ProjectManager
import data.source.task.TaskExternalDataSource
import data.source.task.TaskCsvDataSource
import data.source.task.TaskCsvParser
import data.source.task.TaskManager
import data.source.user.UserExternalDataSource
import data.source.user.UserCsvParser
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.FileValidator
import org.koin.core.qualifier.named
import org.koin.dsl.module
import data.source.mongoDb.MongoClientProvider
import data.source.project.ProjectMongoDBDataSource
import data.source.task.TaskMongoDBDataSource
import data.source.user.UserMongoDBDataSource
import data.utils.PROJECT_COLLECTION
import data.utils.USER_COLLECTION
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

    single(named("projects")) { get<MongoDatabase>().getCollection<ProjectDto>(PROJECT_COLLECTION) }
    single(named("users")) { get<MongoDatabase>().getCollection<UserDto>(USER_COLLECTION) }

    single<UserExternalDataSource> { UserMongoDBDataSource(get(named("users"))) }


    single { TaskManager() }
    single { ProjectManager() }
    single<TaskExternalDataSource> { TaskMongoDBDataSource(get(named("projects"))) }
    single<ProjectExternalDataSource> { ProjectMongoDBDataSource(get(named("projects"))) }


}