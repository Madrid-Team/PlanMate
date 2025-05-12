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
    single { CreateProjectUseCase(get(), get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get(), get(), get(), get()) }
    single { EditProjectNameUseCase(get(), get(), get(), get()) }
    single { EditProjectDescriptionUseCase(get(), get(), get()) }
    single { EditProjectStateUseCase(get(), get(), get()) }
    single { EditProjectTaskStatesUseCase(get(), get(), get()) }
    single { EditProjectProjectStatesUseCase(get(), get(), get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectLogsByIdUseCase(get(), get()) }
    single { GetProjectByIdUseCase(get()) }
    single { ValidateProjectName() }
    single { CreateUserUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { LoginUserUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { TaskValidator() }

    single { DeleteTaskUseCase(get(),get(),get(),) }
    single { CreateTaskUseCase(get(),get(),get(),) }
    single { DisplayAllTasksUseCase(get(), get()) }
    single { EditTaskUseCase(get(),get(),get(),get(),) }
    single { GetTaskLogsUseCase(get(),get(),get(),) }
    single { GetTasksByProjectIdUseCase(get()) }
}