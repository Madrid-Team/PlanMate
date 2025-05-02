package di.modules

import data.source.project.ProjectCsvDataSource
import data.source.project.ProjectDataSource
import data.source.task.TaskCsvDataSource
import data.source.task.TaskDataSource
import data.source.user.UserCsvDataSource
import data.source.user.UserDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<ProjectDataSource> { ProjectCsvDataSource(get(), get(), get()) }
    single<TaskDataSource> { TaskCsvDataSource(get(), get(), get()) }
    single<UserDataSource> { UserCsvDataSource(get(), get(), get()) }
}