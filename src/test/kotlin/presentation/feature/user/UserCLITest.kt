package presentation.feature.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter

class UserCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val createUserCLI = mockk<CreateUserCLI>(relaxed = true)
    private val deleteUserCLI = mockk<DeleteUserCLI>(relaxed = true)
    private lateinit var cli: UserCLI

    @BeforeEach
    fun setUp() {
        cli = UserCLI(inputReader, outputPrinter, createUserCLI, deleteUserCLI)
    }

    @Test
    fun `should show menu and create user when enter 1`() {
        // Given
        every { inputReader.readInput("Select an option: ") } returns "1" andThen "0"

        // When
        cli.show()

        // Then
        verify {
            outputPrinter.printMessage("=== Manage users ===")
            outputPrinter.printMessage("1. Create new user")
            outputPrinter.printMessage("2. Delete new user")
            outputPrinter.printMessage("0. Back")
            createUserCLI.show()
        }
    }

    @Test
    fun `should show menu and delete user when enter 2`() {
        // Given
        every { inputReader.readInput("Select an option: ") } returns "2" andThen "0"

        // When
        cli.show()

        // Then
        verify {
            outputPrinter.printMessage("=== Manage users ===")
            outputPrinter.printMessage("1. Create new user")
            outputPrinter.printMessage("2. Delete new user")
            outputPrinter.printMessage("0. Back")
            deleteUserCLI.show()
        }
    }

    @Test
    fun `should back when enter 0`() {
        // Given
        every { inputReader.readInput("Select an option: ") } returns "0"

        // When
        cli.show()

        // Then
        verify {
            outputPrinter.printMessage("=== Manage users ===")
            outputPrinter.printMessage("1. Create new user")
            outputPrinter.printMessage("2. Delete new user")
            outputPrinter.printMessage("0. Back")
        }
    }

    @Test
    fun `should show error message when enter input incorrectly`() {
        // Given
        every { inputReader.readInput("Select an option: ") } returns "z" andThen "0"

        // When
        cli.show()

        // Then
        verify {
            outputPrinter.printMessage("=== Manage users ===")
            outputPrinter.printMessage("1. Create new user")
            outputPrinter.printMessage("2. Delete new user")
            outputPrinter.printError("Invalid option.")
            outputPrinter.printMessage("0. Back")
        }
    }


}