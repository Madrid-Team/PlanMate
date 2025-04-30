package presentation

import di.modules.cliModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin


fun main() {
    startKoin {
        modules(
            cliModule
        )
    }

    val planMateCLI: PlanMateCLI = getKoin().get()
    planMateCLI.start()
}