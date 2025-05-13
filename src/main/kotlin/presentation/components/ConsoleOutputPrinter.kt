package presentation.components

class ConsoleOutputPrinter : OutputPrinter {
    override fun printMessage(message: String) {
        println(message)
    }

    override fun printError(errorMessage: String) {
        println("[ERROR]: $errorMessage")
    }

    override fun printMenuItems(list: List<String>) {
        list.forEach { this.printMessage(it) }
    }
}