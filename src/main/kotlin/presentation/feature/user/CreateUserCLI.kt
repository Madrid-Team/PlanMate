package presentation.feature.user

import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.usecases.user.CreateUserUseCase
import domain.utils.UserExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateUserCLI(
    private val createUserUseCase: CreateUserUseCase,
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage("=== Create user started ===")
        val userName = collectUsername()
        val password = collectAndValidatePassword()
        val passwordHash = PasswordHasher.hash(password)
        tryCreateUser(userName, passwordHash)
    }

    private fun collectUsername(): String {
        outputPrinter.printMessage("Enter user name:")
        return inputReader.readInput().trim()
    }

    private fun collectAndValidatePassword(): String {
        outputPrinter.printMessage("Enter password (minimum 6 characters):")
        val password = inputReader.readInput()

        if (password.length < 6) {
            outputPrinter.printError("Password must be at least 6 characters")
            return collectAndValidatePassword()
        }

        return password
    }

    private suspend fun tryCreateUser(userName: String, passwordHash: String) {
        try {
            val user = User(
                username = userName,
                passwordHash = passwordHash,
                role = UserRole.MATE.name,
                id = UUID.randomUUID()
            )
            outputPrinter.printMessage("Creating user...")
            createUserUseCase(user)
            outputPrinter.printMessage("User created successfully")
        } catch (e: UserExceptions.UserExist) {
            outputPrinter.printError(e.message.toString())
            showRetryMessage()
        } catch (e: UserExceptions.UserReadWrightException) {
            handleUserReadWriteError(userName, passwordHash, e)
        } catch (_: UserExceptions.UserNotFoundException) {
            outputPrinter.printError("Users file not found")
            showRetryMessage()
        } catch (e: Exception) {
            outputPrinter.printError("Unexpected error: ${e.message}")
            showRetryMessage()
        }
    }

    private suspend fun handleUserReadWriteError(
        userName: String,
        passwordHash: String,
        e: UserExceptions.UserReadWrightException
    ) {
        outputPrinter.printError(e.message.toString())
        outputPrinter.printMessage("1 - Try again with same data\n2 - Enter new data\nAny other key - Exit")
        when (inputReader.readInput()) {
            "1" -> tryCreateUser(userName, passwordHash)
            "2" -> show()
        }
    }

    private suspend fun showRetryMessage() {
        outputPrinter.printMessage("Enter 1 to try again or any other key to exit")
        if (inputReader.readInput() == "1") show()
    }
}