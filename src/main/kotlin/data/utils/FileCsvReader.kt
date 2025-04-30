package data.utils

import java.io.FileNotFoundException
import java.io.IOException

class FileCsvReader(
    private val fileValidator: FileValidator
) {
    fun readProjectCsvFile(): List<String> {
        return try {
            fileValidator.getFile().readLines().dropHeader()
        } catch (e: FileNotFoundException){
            throw e
        }
    }

    private fun List<String>.dropHeader(): List<String> = this.drop(1)
}