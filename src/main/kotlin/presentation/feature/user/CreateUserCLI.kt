package presentation.feature.user

import domain.usecases.user.CreateUserUseCase
import domain.utils.UserExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.enterUserName

class CreateUserCLI(
    private val createUserUseCase: CreateUserUseCase,
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage("=== Create user started ===")
        val userName = inputReader.readInput(String.enterUserName).trim()
        val password = inputReader.readInput("Enter password (minimum 6 characters):")
        tryCreateUser(userName, password)
    }


    private suspend fun tryCreateUser(userName: String, password: String) {
        try {
            outputPrinter.printMessage("Creating user...")
            createUserUseCase.createUser(userName, password)
            outputPrinter.printMessage("User created successfully")
        } catch (e: UserExceptions.UserReadWriteException) {
            handleUserReadWriteError(userName, password, e)
        } catch (e: UserExceptions) {
            outputPrinter.printError("Error: ${e.message}")
            showRetryMessage()
        }
    }

    private suspend fun handleUserReadWriteError(
        userName: String,
        password: String,
        e: UserExceptions.UserReadWriteException
    ) {
        outputPrinter.printError(e.message.toString())
        outputPrinter.printMessage("1 - Try again with same data\n2 - Enter new data\nAny other key - Exit")
        when (inputReader.readInput()) {
            "1" -> tryCreateUser(userName, password)
            "2" -> show()
        }
    }

    private suspend fun showRetryMessage() {
        outputPrinter.printMessage("Enter 1 to try again or any other key to exit")
        if (inputReader.readInput() == "1") show()
    }
}