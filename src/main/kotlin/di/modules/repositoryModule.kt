package di.modules

import data.repository.ProjectRepositoryImpl
import data.repository.TaskRepositoryImpl
import data.repository.UserRepositoryImpl
import data.source.csv.project.ProjectManager
import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<ProjectRepository> { ProjectRepositoryImpl(get() , get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(),get()) }
}
