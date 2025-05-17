package di

import data.repository.AuditLogRepositoryImpl
import data.repository.ProjectRepositoryImpl
import data.repository.TaskRepositoryImpl
import data.repository.UserRepositoryImpl
import domain.repository.AuditLogRepository
import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<ProjectRepository> { ProjectRepositoryImpl(get() , get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(),get()) }
    single<AuditLogRepository> { AuditLogRepositoryImpl(get()) }
}
