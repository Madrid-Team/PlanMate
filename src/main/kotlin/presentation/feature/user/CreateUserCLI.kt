package presentation.feature.user

import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import data.utils.PasswordHasher
import domain.usecases.CreateUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

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
            val user = UserDto(username = userName, passwordHash = passwordHash, role = UserRoleDto.MATE)
            createUserUseCase.createUser(user)
            outputPrinter.printMessage("Login Success")
        } catch (_: Exception) {
            outputPrinter.printMessage("Creating User Failed")
            outputPrinter.printMessage("if you want to try again enter \"1\" else enter anything")
            val userOption = inputReader.readInput()
            if (userOption == "1") show()
        }
    }
}