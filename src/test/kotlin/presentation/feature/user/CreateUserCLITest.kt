package presentation.feature.user

import domain.usecases.user.CreateUserUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter

class CreateUserCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val useCase = mockk<CreateUserUseCase>()
    private lateinit var cli: CreateUserCLI

    @BeforeEach
    fun setUp() {
        cli = CreateUserCLI(useCase, inputReader, outputPrinter)
    }

    @Test
    fun `should create user success when user name and password is correct`() {
        runTest {
            // Given
            val username = "username"
            val password = "password"

            every { inputReader.readInput() } returnsMany listOf(username, password)
            coEvery { useCase.createUser(any()) } returns Unit
            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMessage("=== Create user started ===")
                outputPrinter.printMessage("Enter user name:")
                outputPrinter.printMessage("Enter password (minimum 6 characters):")
                outputPrinter.printMessage("User created successfully")
            }
            verify(exactly = 0) { outputPrinter.printMessage("Creating User Failed") }
        }
    }

    @Test
    fun `should show error message when user name and password is incorrect`() {
        runTest {

            // Given
            val username = "username"
            val password = "password"

            every { inputReader.readInput() } returnsMany listOf(username, password, "z")
            coEvery { useCase.createUser(any()) } throws Exception()
            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMessage("=== Create user started ===")
                outputPrinter.printMessage("Enter user name:")
                outputPrinter.printMessage("Enter password (minimum 6 characters):")
                outputPrinter.printMessage("Creating user...")
                outputPrinter.printMessage("Enter 1 to try again or any other key to exit")
            }
        }
    }
}