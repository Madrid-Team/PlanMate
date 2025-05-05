package presentation.components

interface OutputPrinter {
    fun printMessage(message: String)
    fun printError(errorMessage: String)
}