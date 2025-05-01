package data.utils

import java.io.FileNotFoundException
import java.io.IOException

class FileCsvWriter(
    private val fileValidator: FileValidator
) {
    fun writeProjectToCsvFile(row: String) {
        return fileValidator.getFile().appendText(row)
    }

    fun writeToCsvFile(row: String) {
        try {
            fileValidator.getFile().appendText(row)
        } catch (e: FileNotFoundException) {
            throw e
        } catch (e: Exception) {
            throw IOException("Error writing to CSV file: ${e.message}", e)
        }
    }

    fun updateCsvFile(content: String) {
        try {
            fileValidator.getFile().writeText(content)
        } catch (e: FileNotFoundException) {
            throw e
        } catch (e: Exception) {
            throw IOException("Error writing to CSV file: ${e.message}", e)
        }
    }
}