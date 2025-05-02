package di.modules

import data.repository.ProjectRepositoryImpl
import data.repository.TaskRepositoryImpl
import data.repository.UserRepositoryImpl
import data.source.project.ProjectCsvParser
import data.source.task.TaskCsvParser
import data.source.user.UserCsvParser
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.FileValidator
import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import domain.repository.UserRepository
import org.koin.dsl.module
import java.io.File

val repositoryModule = module {
    single { File("") }
    single { FileValidator(get()) }
    single { FileCsvReader(get()) }
    single { FileCsvWriter(get()) }

    single { TaskCsvParser() }
    single { UserCsvParser() }
    single { ProjectCsvParser() }

    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}