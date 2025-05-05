import di.modules.cliModule
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.assertNotNull

class CliModuleTest : KoinTest {




    @Test
    fun `test InputReader and OutputPrinter are injected and work successfully`() {
        startKoin {
            modules(cliModule)
        }

        val inputReader: InputReader by inject()
        val outputPrinter: OutputPrinter by inject()

        assertNotNull(inputReader)
        assertNotNull(outputPrinter )

        stopKoin() // Stop Koin after test
    }

}
