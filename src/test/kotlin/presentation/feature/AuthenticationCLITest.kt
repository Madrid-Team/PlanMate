package presentation.feature

import com.google.common.truth.Truth.assertThat
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
import presentation.utils.*
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
        runTest {
            // Given
            val username = "user name"
            val password = "password"
            val user = User(id = UUID.randomUUID(), username = username, passwordHash = password, "MATE")
            every { inputReader.readInput() } returnsMany listOf(username, password)
            coEvery { useCase.login(username, password) } returns user

            // When
            cli.login()

            // Then
            verify {
                outputPrinter.printMessage(String.loginHeader)
                outputPrinter.printMessage(String.enterUserName)
                outputPrinter.printMessage(String.enterPassword)
                outputPrinter.printMessage(String.loginSuccess)
            }
            verify(exactly = 0) { outputPrinter.printMessage("Login error:(") }
        }
    }

    @Test
    fun `should handle exception and show Login error message when user name and password is incorrect`() {
        runTest {
            // Given
            val username = "user name"
            val password = "password"
            val invalidUsername = "invalid user name"
            val invalidPassword = "invalid password"
            val errorMessage = "Username or password is incorrect"
            val user = User(id = UUID.randomUUID(), username = username, passwordHash = password, "MATE")

            every { inputReader.readInput() } returnsMany listOf(invalidUsername, invalidPassword, username, password)
            coEvery { useCase.login(invalidUsername, invalidPassword) } throws Exception(errorMessage)
            coEvery { useCase.login(username, password) } returns user

            // When
            val result = cli.login()

            // Then
            verify {
                outputPrinter.printMenuItems(
                    listOf(
                        String.loginError,
                        errorMessage,
                        String.pleaseTryAgain
                    )
                )
                outputPrinter.printMessage(String.loginHeader)
                outputPrinter.printMessage(String.enterUserName)
                outputPrinter.printMessage(String.enterPassword)
                outputPrinter.printMessage(String.loginSuccess)
            }
            assertThat(result).isEqualTo(user)
        }
    }
}