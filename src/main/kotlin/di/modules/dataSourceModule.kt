package di.modules

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.project.ProjectDto
import data.source.project.ProjectCsvDataSource
import data.source.project.ProjectCsvParser
import data.source.project.ProjectDataSource
import data.source.task.*
import data.source.user.UserCsvDataSource
import data.source.user.UserCsvParser
import data.source.user.UserDataSource
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.FileValidator
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.madrid.data.source.mongoDb.MongoClientProvider
import org.madrid.data.source.project.ProjectMongoDBDataSource
import org.madrid.data.source.project.RemoteProjectDataSource
import org.madrid.data.source.task.TaskMongoDBDataSource
import org.madrid.data.utils.PROJECT_COLLECTION
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


    single<ProjectDataSource> { ProjectCsvDataSource(get(named("projectReader")), get(named("projectWriter")), get()) }
    single<UserDataSource> { UserCsvDataSource(get(named("userReader")), get(named("userWriter")), get()) }
    single<ExternalTaskDataSource> { TaskCsvDataSource(get(), get(named("taskWriter")), get(named("taskReader")),get()) }

    single { MongoClientProvider() }
    single { get<MongoClientProvider>().getDatabase() }
    single { get<MongoDatabase>().getCollection<ProjectDto>(PROJECT_COLLECTION) }

    single<RemoteProjectDataSource> { ProjectMongoDBDataSource(get())}

    single { TaskManager() }
    single<ExternalTaskDataSource> { TaskMongoDBDataSource(get()) }

}