package presentation

import di.modules.cliModule
import di.modules.dataSourceModule
import di.modules.repositoryModule
import di.modules.useCasesModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin


fun main() {
    startKoin { modules(cliModule, useCasesModule, repositoryModule, dataSourceModule) }

    val planMateCLI: PlanMateCLI = getKoin().get()
    planMateCLI.start()
}