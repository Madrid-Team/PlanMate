package di.modules

import domain.usecases.logs.CreateLogUseCase
import domain.usecases.user.CreateUserUseCase
import domain.usecases.user.DeleteUserUseCase
import domain.usecases.task.DisplayAllTasksUseCase
import domain.usecases.user.LoginUserUseCase
import domain.usecases.user.CreateUserUseCase
import domain.usecases.user.DeleteUserUseCase
import domain.usecases.DisplayAllTasksUseCase
import domain.usecases.user.LoginUserUseCase
import domain.usecases.project.*
import domain.usecases.task.*
import org.koin.dsl.module

val useCasesModule = module {
    single { CreateLogUseCase() }
    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { EditProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectLogsByIdUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }

    single { CreateUserUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { LoginUserUseCase(get()) }

    single { DeleteTaskUseCase(get()) }
    single { CreateTaskUseCase(get()) }
    single { DisplayAllTasksUseCase(get(), get()) }
    single { EditTaskUseCase(get()) }
    single { GetLogsUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
}