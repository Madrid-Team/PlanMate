package presentation.feature

import domain.models.authentication.User
import domain.usecases.CreateUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class AuthenticationCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val useCase: CreateUserUseCase,
//    private val useCase: LoginUseCase
) {
    private val currentUser: User? = null
    fun login() {
    }

    fun getCurrentUser(): User? = currentUser

    fun createUser(){}

}