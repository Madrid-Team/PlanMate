package presentation.feature.user

import data.mapper.toDto
import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.usecases.user.CreateUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateUserCLI(
    private val createUserUseCase: CreateUserUseCase,
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter
) {
    fun show() {
        outputPrinter.printMessage("=== Create user started ===")
        outputPrinter.printMessage("Enter user name:")
        val userName = inputReader.readInput()
        outputPrinter.printMessage("Enter password:")
        val password = inputReader.readInput()
        val passwordHash = PasswordHasher.hash(password)
        try {
            val user = User(
                username = userName, passwordHash = passwordHash, role = UserRole.MATE.name,
                id = UUID.randomUUID()
            )
            createUserUseCase.createUser(user.toDto())
            outputPrinter.printMessage("Login Success")
        } catch (_: Exception) {
            outputPrinter.printMessage("Creating User Failed")
            outputPrinter.printMessage("if you want to try again enter \"1\" else enter anything")
            val userOption = inputReader.readInput()
            if (userOption == "1") show()
        }
    }
}