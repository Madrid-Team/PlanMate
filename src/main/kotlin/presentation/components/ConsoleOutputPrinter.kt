package presentation.components

class ConsoleOutputPrinter : OutputPrinter {
    override fun printMessage(message: String) {
        println(message)
    }

    override fun printError(errorMessage: String) {
        println("[ERROR]: $errorMessage")
    }
}