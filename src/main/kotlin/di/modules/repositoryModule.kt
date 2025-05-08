package di.modules

import data.repository.TaskRepositoryImpl
import data.repository.UserRepositoryImpl
import data.source.project.ProjectMemoryDataSource
import data.utils.ProjectRepositoryImpl
import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<ProjectRepository> { ProjectRepositoryImpl(get(), get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(),get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    single  { ProjectMemoryDataSource() }

}
