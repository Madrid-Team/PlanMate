package data.utils

import java.io.FileNotFoundException
import java.io.IOException

class FileCsvReader(
    private val fileValidator: FileValidator
) {
    fun readProjectCsvFile(): List<String> {
        return fileValidator.getFile().readLines().dropHeader()
    }

    fun readCsvFile(): List<String> {
        return try {
            fileValidator.checkFile().readLines().dropHeader()
        } catch (e: IOException){
            throw e
        }
    }
    
    private fun List<String>.dropHeader(): List<String> = this.drop(1)
}