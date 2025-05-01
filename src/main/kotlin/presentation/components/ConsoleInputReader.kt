package presentation.components

class ConsoleInputReader : InputReader {
    override fun readInput(hint: String): String {
        if (hint.isNotEmpty()){
            println(hint)
        }
        return readlnOrNull() ?: ""
    }
}