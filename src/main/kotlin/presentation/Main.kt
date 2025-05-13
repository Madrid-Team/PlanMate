package presentation

import di.modules.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin


fun main() {
    startKoin { modules(cliModule, useCasesModule, repositoryModule, dataSourceModule, validationModule) }

    val planMateCLI: PlanMateCLI = getKoin().get()
    val coroutineScope  = CoroutineScope(Dispatchers.Default)
    coroutineScope.launch {

        planMateCLI.start()
    }
}