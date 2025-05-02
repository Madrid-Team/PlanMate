package presentation.feature

import data.utils.PasswordHasher
import domain.mapper.toDomain
import domain.models.logs.CurrentUser
import domain.usecases.LoginUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class AuthenticationCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val loginUserUseCase: LoginUserUseCase
) {

    fun login() {
        outputPrinter.printMessage("=== Login ===")
        outputPrinter.printMessage("Enter user name:")
        val userName = inputReader.readInput()
        outputPrinter.printMessage("Enter password:")
        val password = inputReader.readInput()
        val passwordHash = PasswordHasher.hash(password)
        try {
            val success = loginUserUseCase.invoke(userName, passwordHash)
            outputPrinter.printMessage("Login Success")
            CurrentUser.setCurrentUser(success!!.toDomain())
        } catch (e: Exception) {
            outputPrinter.printMessage(e.message.toString())
            outputPrinter.printMessage("Login error:(")
            outputPrinter.printMessage("if you want to try again enter \"1\" else enter anything")
            val userOption = inputReader.readInput()
            if (userOption == "1") login()
        }
    }
}