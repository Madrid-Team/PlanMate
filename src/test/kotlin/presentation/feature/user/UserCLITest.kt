package presentation.feature.user

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.back
import presentation.utils.createNewUser
import presentation.utils.deleteUser
import presentation.utils.invalidOption
import presentation.utils.manageUsersMenu
import presentation.utils.selectOption

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
        runTest {
            // Given
            every { inputReader.readInput(String.selectOption) } returns "1" andThen "0"

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMenuItems(
                    listOf(
                        String.manageUsersMenu,
                        String.createNewUser,
                        String.deleteUser,
                        String.back
                    )
                )
            }
            coVerify {
                createUserCLI.show()
            }
        }
    }

    @Test
    fun `should show menu and delete user when enter 2`() {
        runTest {
            // Given
            every { inputReader.readInput(String.selectOption) } returns "2" andThen "0"

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMenuItems(
                    listOf(
                        String.manageUsersMenu,
                        String.createNewUser,
                        String.deleteUser,
                        String.back
                    )
                )
            }
            coVerify { deleteUserCLI.show() }
        }
    }

    @Test
    fun `should back when enter 0`() {
        runTest {
            // Given
            every { inputReader.readInput(String.selectOption) } returns "0"

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMenuItems(
                    listOf(
                        String.manageUsersMenu,
                        String.createNewUser,
                        String.deleteUser,
                        String.back
                    )
                )
            }
        }
    }

    @Test
    fun `should show error message when enter input incorrectly`() {
        runTest {
            // Given
            every { inputReader.readInput(String.selectOption) } returns "z" andThen "0"

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMenuItems(
                    listOf(
                        String.manageUsersMenu,
                        String.createNewUser,
                        String.deleteUser,
                        String.back
                    )
                )
                outputPrinter.printError(String.invalidOption)
            }
        }
    }
}