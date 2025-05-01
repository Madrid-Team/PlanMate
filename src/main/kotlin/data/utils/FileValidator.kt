package data.utils

import java.io.File
import java.io.FileNotFoundException

class FileValidator(
  private val file: File
) {
    fun getFile(): File {
        if (file.exists()) {
            return file
        } else
            throw FileNotFoundException("Project file does not exist")
    }
}