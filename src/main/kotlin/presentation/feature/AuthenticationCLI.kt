package presentation.feature

import domain.models.authentication.User
import domain.usecases.LoginUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class AuthenticationCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val loginUserUseCase: LoginUserUseCase
) {
    private var currentUser: User? = null

    fun login() {
    }

    fun getCurrentUser(): User? = currentUser

    fun createUser(){}

}