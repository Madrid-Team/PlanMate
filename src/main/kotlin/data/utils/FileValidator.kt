package data.utils

import java.io.File
import java.io.IOException

class FileValidator(
    val file: File
) {
    fun checkFile(): File {
        return if (file.exists()) {
            file
        } else
            createCsvFileWithHeader()
    }

    fun createCsvFileWithHeader(): File {
        try {

            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }

            file.createNewFile()

            file.name.appendHeader(file)

            return file
        } catch (e: Exception) {
            throw IOException("Error creating CSV file: ${e.message}", e)
        }
    }

}