package di.modules

import domain.usecases.*
import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.EditProjectUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { EditProjectUseCase(get()) }

    single { CreateUserUseCase() }
    single { DeleteUser(get()) }
    single { LoginUserUseCase(get(), get()) }

    single { DeleteTaskUseCase(get()) }
    single { CreateTaskUseCase(get()) }
    single { DisplayAllTasksUseCase(get(), get()) }
    single { EditTaskUseCase(get()) }

}