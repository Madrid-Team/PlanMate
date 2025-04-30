package di.modules

import org.koin.dsl.module
import presentation.PlanMateCLI
import presentation.components.ConsoleInputReader
import presentation.components.ConsoleOutputPrinter
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.AuthenticationCLI
import presentation.feature.ProjectAuditLogCLI
import presentation.feature.TaskAuditLogCLI
import presentation.feature.tasks.*

val cliModule = module {
    single<InputReader> { ConsoleInputReader() }
    single<OutputPrinter> { ConsoleOutputPrinter() }

    single { CreateTaskCLI(get(), get(), get(), get()) }
    single { DeleteTaskCLI(get(), get(), get(), get()) }
    single { EditTaskCLI(get(), get(), get(), get()) }
    single { TaskCLI(get(), get(), get(), get()) }
    single { TaskView() }
    single { ProjectAuditLogCLI(get(), get()) }
    single { TaskAuditLogCLI(get(), get()) }
    single { AuthenticationCLI(get(), get()) }
    single { PlanMateCLI(get(), get(), get(), get(), get(), get(), get(), get()) }


}
