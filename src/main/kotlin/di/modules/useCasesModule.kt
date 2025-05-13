package di.modules

import domain.usecases.logs.CreateLogUseCase
import domain.usecases.project.*
import domain.usecases.task.*
import domain.usecases.user.*
import org.koin.dsl.module

val useCasesModule = module {
    single { ValidateNameUseCase() }
    single { ValidatePasswordUseCase() }
    single { PasswordHashUseCase() }
    single { ValidateAdminRoleUseCase() }
    single { CreateLogUseCase(get()) }
    single { CreateProjectUseCase(get(), get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get(), get(), get()) }
    single { CreateProjectUseCase(get(), get()) }
    single { CreateProjectUseCase(get(), get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get(), get(), get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectLogsByIdUseCase(get(), get()) }
    single { GetProjectByIdUseCase(get()) }
    single { CreateUserUseCase(get(), get(), get(),get()) }
    single { DeleteUserUseCase(get(), get()) }
    single { LoginUserUseCase(get(), get(), get()) }
    single { DeleteTaskUseCase(get(),get(),get()) }
    single { CreateTaskUseCase(get(), get(), get()) }
    single { EditTaskUseCase(get(), get(), get(), get()) }
    single { GetTaskLogsUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
    single { TaskValidator() }
    single { ProjectValidator() }
    single { GetTaskByIdUseCase(get()) }

}