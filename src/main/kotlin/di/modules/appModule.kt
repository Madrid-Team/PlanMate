package di.modules

import org.koin.dsl.module
import presentation.PlanMateConsoleUI
import presentation.components.ConsoleInputReader
import presentation.components.ConsoleOutputPrinter
import presentation.components.InputReader
import presentation.components.OutputPrinter

val appModule = module {
    single<InputReader> { ConsoleInputReader() }
    single<OutputPrinter> { ConsoleOutputPrinter() }
    single { PlanMateConsoleUI(get(), get()) }
}