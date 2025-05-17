package di

import domain.usecases.project.*
import domain.usecases.task.*
import domain.usecases.user.*
import org.koin.dsl.module

val useCasesModule = module {
    single { ValidateNameUseCase() }
    single { ValidatePasswordUseCase() }
    single { PasswordHashUseCase() }
    single { ValidateAdminRoleUseCase() }
    single { TaskValidator() }
    single { ProjectValidator() }

    single { GetAllProjectsUseCase(get()) }
    single { GetProjectLogsByIdUseCase(get(), get()) }
    single { GetProjectByIdUseCase(get()) }
    single { GetTaskLogsUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }

    factory { CreateProjectUseCase(get(), get()) }
    factory { DeleteProjectUseCase(get(), get()) }
    factory { EditProjectUseCase(get(), get(), get()) }
    factory { CreateUserUseCase(get(), get(), get(), get()) }
    factory { DeleteUserUseCase(get(), get()) }
    factory { LoginUserUseCase(get(), get(), get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { CreateTaskUseCase(get(), get()) }
    factory { EditTaskUseCase(get(), get()) }
}