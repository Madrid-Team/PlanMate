package di.modules

import org.koin.dsl.module
import presentation.PlanMateCLI
import presentation.components.ConsoleInputReader
import presentation.components.ConsoleOutputPrinter
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.AuthenticationCLI
import org.madrid.presentation.feature.tasks.TaskAuditLogCLI
import presentation.feature.admin.AdminCLI
import presentation.feature.projects.*
import presentation.feature.tasks.*
import presentation.feature.user.CreateUserCLI
import presentation.feature.user.DeleteUserCLI
import presentation.feature.user.UserCLI

val cliModule = module {
    single<InputReader> { ConsoleInputReader() }
    single<OutputPrinter> { ConsoleOutputPrinter() }

    single { CreateTaskCLI(get(), get(), get(), get(), get()) }
    single { DeleteTaskCLI(get(), get(), get()) }
    single { EditTaskCLI(get(), get(), get(), get()) }
    single { TaskCLI(get(), get(), get(), get(), get(), get()) }
    single { TaskView(get(), get(), get()) }
    single { ProjectAuditLogCLI(get(), get(), get()) }
    single { TaskAuditLogCLI(get(), get(), get()) }
    single { AuthenticationCLI(get(), get(), get()) }
    single { PlanMateCLI(get(), get(), get(), get(), get(), get(), get()) }
    single { ProjectCLI(get(), get(), get(), get(), get(), get(), get(), get()) }
    single { CreateProjectCLI(get(), get(), get(), get()) }
    single { DeleteProjectCLI(get(), get(), get()) }
    single { EditProjectCLI(get(), get(), get(), get(), get()) }
    single { UserCLI(get(), get(), get(), get()) }
    single { CreateUserCLI(get(), get(), get()) }
    single { DeleteUserCLI(get(), get(), get()) }
    single { ProjectView(get()) }
    single { AdminCLI(get(), get()) }
}
