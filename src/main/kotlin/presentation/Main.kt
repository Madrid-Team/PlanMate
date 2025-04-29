package presentation

import di.modules.appModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin


fun main() {
    startKoin { modules(appModule) }

    val planMateConsoleUI: PlanMateConsoleUI = getKoin().get()
    planMateConsoleUI.start()
}