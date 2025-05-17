package di

import domain.usecases.logs.AddAuditLogUseCase
import domain.usecases.project.*
import domain.usecases.task.*
import domain.usecases.user.*
import org.koin.dsl.module

val useCasesModule = module {
    single { ValidateNameUseCase() }
    single { ValidatePasswordUseCase() }
    single { PasswordHashUseCase() }
    single { ValidateAdminRoleUseCase() }
    single { CreateProjectUseCase(get(), get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get(), get(), get(), get(), get()) }
    single { CreateProjectUseCase(get(), get()) }
    single { CreateProjectUseCase(get(), get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get(), get(), get(), get(), get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectLogsByIdUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { CreateUserUseCase(get(), get(), get(), get()) }
    single { DeleteUserUseCase(get(), get()) }
    single { LoginUserUseCase(get(), get(), get()) }
    single { DeleteTaskUseCase(get()) }
    single { CreateTaskUseCase(get(), get()) }
    single { EditTaskUseCase(get(), get(), get(), get(), get()) }
    single { GetTaskLogsUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
    single { TaskValidator() }
    single { ProjectValidator() }
    single { AddAuditLogUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }

}