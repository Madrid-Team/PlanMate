package di.modules

import domain.usecases.logs.CreateLogUseCase
import domain.usecases.task.DisplayAllTasksUseCase
import domain.usecases.user.CreateUserUseCase
import domain.usecases.user.DeleteUserUseCase
import domain.usecases.user.LoginUserUseCase
import domain.usecases.project.*
import domain.usecases.task.*
import domain.validation.ValidateProjectName
import org.koin.dsl.module

val useCasesModule = module {
    single { CreateLogUseCase() }
    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { EditProjectUseCase(get(), get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectLogsByIdUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { ValidateProjectName() }
    single { CreateUserUseCase(get(),get(),get()) }
    single { DeleteUserUseCase(get(),get())}
    single { LoginUserUseCase(get(),get(),get()) }

    single { DeleteTaskUseCase(get()) }
    single { CreateTaskUseCase(get()) }
    single { DisplayAllTasksUseCase(get(), get()) }
    single { EditTaskUseCase(get()) }
    single { GetTaskLogsUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
}