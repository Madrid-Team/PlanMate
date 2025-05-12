package presentation.feature

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
        try {
            loginUserUseCase.login(userName, password)
            outputPrinter.printMessage(String.loginSuccess)
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