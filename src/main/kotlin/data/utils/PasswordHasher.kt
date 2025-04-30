package data.utils

import java.security.MessageDigest

object PasswordHasher {

    fun hash(password: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verify(password: String, hashedPassword: String): Boolean {
        return hash(password) == hashedPassword
    }
}
