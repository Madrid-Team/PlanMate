package presentation.feature

import domain.models.authentication.User
import domain.usecases.user.LoginUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class AuthenticationCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val loginUserUseCase: LoginUserUseCase
) {
    suspend fun login(): User {
        outputPrinter.printMessage(String.loginHeader)
        outputPrinter.printMessage(String.enterUserName)
        val userName = inputReader.readInput()
        outputPrinter.printMessage(String.enterPassword)
        val password = inputReader.readInput()
        return  try {
          val user = loginUserUseCase.login(userName, password)
          outputPrinter.printMessage(String.loginSuccess)
          user
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