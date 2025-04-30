package data.utils

class FileCsvReader(
    private val fileValidator: FileValidator
) {
    fun readProjectCsvFile(): List<String> {
        return fileValidator.getFile().readLines().dropHeader()
    }

    private fun List<String>.dropHeader(): List<String> = this.drop(1)
}