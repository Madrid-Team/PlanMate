package data.utils

class FileCsvWriter(
    private val fileValidator: FileValidator
) {
    fun writeProjectToCsvFile(row: String) {
        return fileValidator.getFile().appendText(row)
    }
}