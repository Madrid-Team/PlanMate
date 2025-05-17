package presentation

import data.utils.PasswordHasher
import domain.models.authentication.User
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.AuthenticationCLI
import presentation.feature.admin.AdminCLI
import presentation.feature.mate.MateCLI
import presentation.utils.*
import java.util.*

class PlanMateCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var authenticationCLI: AuthenticationCLI
    private lateinit var mateCLI: MateCLI
    private lateinit var adminCLI: AdminCLI
    private lateinit var planMateCLI: PlanMateCLI


    @BeforeEach
    fun setUp() {
        inputReader = mockk(relaxed = true)
        outputPrinter = mockk(relaxed = true)
        authenticationCLI = mockk(relaxed = true)
        mateCLI = mockk(relaxed = true)
        adminCLI = mockk(relaxed = true)
        planMateCLI = PlanMateCLI(inputReader, outputPrinter, authenticationCLI, adminCLI, mateCLI)
    }

    @Test
    fun `should print welcome  and main menu and exit message when app starts when select 0`() {
        runTest {
            // Given
            every { inputReader.readInput(any()) } returns "0"

            // when
            planMateCLI.start()

            // then
            verify {
                outputPrinter.printMessage(String.welcomeMessage)
                outputPrinter.printMessage(String.mainMenu)
                outputPrinter.printMessage(String.login)
                outputPrinter.printMessage(String.exit)
                outputPrinter.printMessage(String.goodbye)
            }
        }
    }

    @Test
    fun `should print invalid option message when app starts when select invalid option`() {
        runTest {
            // Given
            every { inputReader.readInput("Select an option: ") } returns "z" andThen "0"

            // when
            planMateCLI.start()

            // then
            verify {
                outputPrinter.printMessage(String.welcomeMessage)
                outputPrinter.printMessage(String.mainMenu)
                outputPrinter.printMessage(String.login)
                outputPrinter.printMessage(String.exit)
                outputPrinter.printError(String.invalidOption)
                outputPrinter.printMessage(String.goodbye)
            }
        }
    }


    @Test
    fun `should show admin menu when admin login success`() {
        runTest {
            // Given
            val username = "admin"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "ADMIN")
            coEvery { authenticationCLI.login() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "0")
            // When
            planMateCLI.start()


            // Then
            verify {
                outputPrinter.printMessage("=== Welcome to PlanMate ===")
                outputPrinter.printMessage("=== === Main Menu === ===")
                outputPrinter.printMessage("1. Log in")
                outputPrinter.printMessage("0. Exit")
                outputPrinter.printMessage("\nWelcome, ${user.username}! (Role: ${user.role})")

            }
            coVerify { adminCLI.showAdminMenu() }
        }
    }

    @Test
    fun `should show mate menu when mate login success`() {
        runTest {
            // Given
            val username = "mate"
            val password = "password"
            val passwordHash = PasswordHasher.hash(password)
            val user = User(id = UUID.randomUUID(), username, passwordHash, "MATE")
            coEvery { authenticationCLI.login() } returns user
            every { inputReader.readInput(any()) } returnsMany listOf("1", username, password, "0")
            // When
            planMateCLI.start()


            // Then
            verify {
                outputPrinter.printMessage("=== Welcome to PlanMate ===")
                outputPrinter.printMessage("=== === Main Menu === ===")
                outputPrinter.printMessage("1. Log in")
                outputPrinter.printMessage("0. Exit")
                outputPrinter.printMessage("\nWelcome, ${user.username}! (Role: ${user.role})")

            }
            coVerify { mateCLI.showMateMenu() }
        }
    }

}
