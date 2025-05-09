package presentation.feature

import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.usecases.user.LoginUserUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*
import kotlin.test.Test

class AuthenticationCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val useCase = mockk<LoginUserUseCase>()
    private lateinit var cli: AuthenticationCLI

    @BeforeEach
    fun setUp() {
        cli = AuthenticationCLI(inputReader, outputPrinter, useCase)
    }

    @Test
    fun `should login success when user name and password is correct`() {
        // Given
        val username = "user name"
        val password = "password"
        val passwordHash = PasswordHasher.hash(password)
        val user = User(id = UUID.randomUUID(), username, passwordHash, "MATE")

        every { inputReader.readInput() } returnsMany listOf(username, password)
        coEvery { useCase.invoke(username, passwordHash) } returns user

        // When
        cli.login()

        // Then
        verify {
            outputPrinter.printMessage("=== Login ===")
            outputPrinter.printMessage("Enter user name:")
            outputPrinter.printMessage("Enter password:")
            outputPrinter.printMessage("Login Success")
        }
        verify(exactly = 0) { outputPrinter.printMessage("Login error:(") }
    }

    @Test
    fun `should handle exception and show Login error message when user name and password is incorrect`() {
        // Given
        val username = "user name"
        val password = "password"
        val passwordHash = PasswordHasher.hash(password)

        every { inputReader.readInput() } returnsMany listOf(username, password, "z") // 2 = don't retry
        coEvery { useCase.invoke(username, passwordHash) } throws Exception()

        // When
        cli.login()

        // Then
        verify {
            outputPrinter.printMessage("Login error:(")
            outputPrinter.printMessage("if you want to try again enter \"1\" else enter anything")
        }
    }

    @Test
    fun `should retry login when exception occurs and user enters 1`() {
        // Given
        val correctUsername = "user name 1"
        val correctPassword = "password 1"
        val incorrectUsername = "user name 2"
        val incorrectPassword = "password 2"
        val correctHash = PasswordHasher.hash(correctPassword)
        val incorrectHash = PasswordHasher.hash(incorrectPassword)
        val user = User(id = UUID.randomUUID(), correctUsername, correctHash, "MATE")

        every { inputReader.readInput() } returnsMany listOf(
            incorrectUsername,
            incorrectPassword,
            "1",
            correctUsername,
            correctPassword
        )
        coEvery { useCase.invoke(incorrectUsername, incorrectHash) } throws Exception()
        coEvery { useCase.invoke(correctUsername, correctHash) } returns user

        // When
        cli.login()

        // Then
        verify(exactly = 2) {
            outputPrinter.printMessage("Enter user name:")
            outputPrinter.printMessage("Enter password:")
        }
        verify(exactly = 1) { outputPrinter.printMessage("Login error:(") }
    }

    @Test
    fun `shouldn't retry login when exception occurs and user don't enters 1`() {
        // Given
        val incorrectUsername = "user name 2"
        val incorrectPassword = "password 2"
        val incorrectHash = PasswordHasher.hash(incorrectPassword)

        every { inputReader.readInput() } returnsMany listOf(incorrectUsername, incorrectPassword, "z")
        coEvery { useCase.invoke(incorrectUsername, incorrectHash) } throws Exception()

        // When
        cli.login()

        // Then
        verify(exactly = 1) {
            outputPrinter.printMessage("Enter user name:")
            outputPrinter.printMessage("Enter password:")
            outputPrinter.printMessage("Login error:(")
        }
    }
}