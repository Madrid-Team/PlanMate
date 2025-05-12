package presentation.feature

import data.utils.PasswordHasher
import domain.models.logs.CurrentUser
import domain.usecases.user.LoginUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class AuthenticationCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val loginUserUseCase: LoginUserUseCase
) {
    suspend fun login() {
        outputPrinter.printMessage(String.loginHeader)
        outputPrinter.printMessage(String.enterUserName)
        val userName = inputReader.readInput()
        outputPrinter.printMessage(String.enterPassword)
        val password = inputReader.readInput()
        val passwordHash = PasswordHasher.hash(password)
        try {
            val success = loginUserUseCase.invoke(userName, passwordHash)
            outputPrinter.printMessage(String.loginSuccess)
            CurrentUser.setCurrentUser(success)
        } catch (e: Exception) {
            outputPrinter.printMenuItems(
                listOf(
                    String.loginError,
                    e.message.toString(),
                    String.pleaseTryAgain
                )
            )
            login()
        }
    }
}