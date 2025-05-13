package di.modules

 import domain.usecases.task.DisplayAllTasksUseCase
import domain.usecases.user.CreateUserUseCase
import domain.usecases.user.DeleteUserUseCase
import domain.usecases.user.LoginUserUseCase
import domain.usecases.project.*
import domain.usecases.task.*
import org.koin.dsl.module

val useCasesModule = module {
    single { CreateProjectUseCase(get(), get()) }
     single { CreateProjectUseCase(get(), get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get(), get(), get()) }
    single { GetAllProjectsUseCase(get()) }
    single { GetProjectLogsByIdUseCase(get(), get()) }
    single { GetProjectByIdUseCase(get()) }
    single { CreateUserUseCase(get(),get()) }
    single { DeleteUserUseCase(get()) }
    single { LoginUserUseCase(get()) }
    single { DeleteTaskUseCase(get() ) }
    single { CreateTaskUseCase(get(),get(),get(),) }
    single { DisplayAllTasksUseCase(get(),get()) }
    single { EditTaskUseCase(get(),get(),get(),get()) }
    single { GetTaskLogsUseCase(get() ) }
    single { GetTasksByProjectIdUseCase(get()) }
    single { TaskValidator() }
    single { ProjectValidator() }
    single { GetTaskByIdUseCase(get()) }

}