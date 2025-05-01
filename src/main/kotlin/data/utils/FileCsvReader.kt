package data.utils

import java.io.FileNotFoundException


class FileCsvReader(
    private val fileValidator: FileValidator
) {
    fun readCsvFile(): List<String> {
        return try {
            fileValidator.getFile().readLines().dropHeader()
        } catch (e: FileNotFoundException){
            throw e
        }
    }

    private fun List<String>.dropHeader(): List<String> = this.drop(1)
}