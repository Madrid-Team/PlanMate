package data.utils

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class FileCsvWriter(
    private val fileValidator: FileValidator
) {
    fun writeToCsvFile(row:String) {
        try {
            fileValidator.getFile().appendText(row)
        }catch (e: FileNotFoundException) {
            throw e
        }catch (e: Exception) {
            throw IOException("Error writing to CSV file: ${e.message}", e)
        }
    }
}