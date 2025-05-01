package presentation.components

class ConsoleOutputPrinter : OutputPrinter {
    override fun printMessage(message: String) {
        print(message)
    }

    override fun printError(errorMessage: String) {
        print("[ERROR]: $errorMessage")
    }
}