package data.utils

import java.io.IOException

class FileCsvWriter(
    private val fileValidator: FileValidator
) {
    fun writeProjectToCsvFile(row: String) {
        return fileValidator.checkFile().appendText(row)
    }

    fun writeToCsvFile(row: String) {
        try {
            fileValidator.checkFile().appendText("$row \n")
        }  catch (e: Exception) {
            throw IOException("Error writing to CSV file: ${e.message}", e)
        }
    }

    fun updateCsvFile(content: String) {
        try {
            fileValidator.checkFile().writeText(content)
        }  catch (e: Exception) {
            throw IOException("Error writing to CSV file: ${e.message}", e)
        }
    }
}