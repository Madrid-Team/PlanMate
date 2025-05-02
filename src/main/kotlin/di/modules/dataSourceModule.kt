package di.modules

import data.source.project.ProjectCsvDataSource
import data.source.project.ProjectDataSource
import data.source.task.TaskCsvDataSource
import data.source.task.TaskDataSource
import data.source.user.UserCsvDataSource
import data.source.user.UserDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    single<ProjectDataSource> { ProjectCsvDataSource(get(named("projectReader")), get(named("projectWriter")), get()) }
    single<UserDataSource> { UserCsvDataSource(get(named("userReader")), get(named("userWriter")), get()) }
    single<TaskDataSource> { TaskCsvDataSource(get(), get(named("taskWriter")), get(named("taskReader"))) }
}