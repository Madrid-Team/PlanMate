package presentation

import di.modules.*
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin


suspend fun main() {
    startKoin { modules(cliModule, useCasesModule, repositoryModule, dataSourceModule, validationModule) }

    val planMateCLI: PlanMateCLI = getKoin().get()
    planMateCLI.start()
}