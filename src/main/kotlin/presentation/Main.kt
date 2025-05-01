package presentation

import di.modules.cliModule
import di.modules.useCasesModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin


fun main() {
    startKoin { modules(cliModule, useCasesModule) }

    val planMateCLI: PlanMateCLI = getKoin().get()
    planMateCLI.start()
}