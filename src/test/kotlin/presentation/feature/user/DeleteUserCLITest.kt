package presentation.feature.user

import data.source.user.CurrentUserProvider
import domain.models.authentication.User
import domain.models.logs.CurrentUser
import domain.usecases.user.DeleteUserUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.deleteUserStarted
import presentation.utils.deletedFailed
import presentation.utils.deletedSuccess
import presentation.utils.enterUserId
import presentation.utils.pressOneToTryAgain

class DeleteUserCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val deleteUserUseCase = mockk<DeleteUserUseCase>()
    private val currentUserProvider = mockk<CurrentUserProvider>()
    private lateinit var cli: DeleteUserCLI

    @BeforeEach
    fun setUp() {
        cli = DeleteUserCLI(inputReader, outputPrinter, deleteUserUseCase, currentUserProvider)
    }

    @Test
    fun `should delete user successfully when userId is exist`() {
        runTest {
            // Given
            every { inputReader.readInput() } returns "2"
            every { currentUserProvider.getCurrentUser().id } returns "1"
            coEvery { deleteUserUseCase.deleteUser("1", "2") } just runs

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMenuItems(listOf(String.deleteUserStarted, String.enterUserId))
                outputPrinter.printMessage(String.deletedSuccess)
            }
            verify(exactly = 0) { outputPrinter.printMessage("Deleted Failed") }
        }
    }

    @Test
    fun `should show error message when delete user fails`() {
        runTest {
            // Given
            val errorMessage = "Something went wrong"
            every { inputReader.readInput() } returnsMany listOf("2", "z")
            every { currentUserProvider.getCurrentUser().id } returns "1"
            coEvery { deleteUserUseCase.deleteUser("1", "2") } throws Exception(errorMessage)

            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMenuItems(listOf(String.deleteUserStarted, String.enterUserId))
                outputPrinter.printMenuItems(listOf(String.deletedFailed, errorMessage, String.pressOneToTryAgain))

            }
        }
    }
}