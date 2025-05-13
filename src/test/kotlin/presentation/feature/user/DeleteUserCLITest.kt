package presentation.feature.user

import data.source.user.CurrentUserProvider
import domain.models.authentication.User
import domain.models.logs.CurrentUser
import domain.usecases.user.DeleteUserUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.deleteUserStarted
import presentation.utils.deletedSuccess
import presentation.utils.enterUserId
import kotlin.test.assertNotNull

class DeleteUserCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val useCase = mockk<DeleteUserUseCase>()
    private lateinit var cli: DeleteUserCLI
    private  var currentUserProvider: CurrentUserProvider=mockk()

    @BeforeEach
    fun setUp() {
        cli = DeleteUserCLI(inputReader, outputPrinter, useCase,currentUserProvider)
        mockkObject(CurrentUser)
    }

    @Test
    fun `should delete user successfully when userId is exist`() {
        runTest {
            // Given
            val mockCurrentUser = mockk<User>()

            every { inputReader.readInput() } returns "2"
            every { mockCurrentUser.id.toString() } returns "1"
            every { CurrentUser.getCurrentUser() } returns mockCurrentUser
            every { outputPrinter.printMessage(String.deletedSuccess) } returns Unit
            coEvery { useCase.deleteUser("1", "2") } returns Unit

            // When
           assertDoesNotThrow {   cli.show()}

            // Then
            val expectedOutput = """
            ${String.deleteUserStarted}
            ${String.enterUserId}
            ${String.deletedSuccess}
        """.trimIndent() + "\n"

            verify(exactly = 1) {  outputPrinter.printMenuItems(listOf(String.deleteUserStarted, String.enterUserId)) }

            assertDoesNotThrow {   outputPrinter.printMessage(expectedOutput)}
            assertNotNull(outputPrinter.printMessage(expectedOutput))

        }
    }

    @Test
    fun `should show error message when delete user fails`() {
        runTest {
            // Given
            val mockCurrentUser = mockk<User>()
            every { inputReader.readInput() } returnsMany listOf("2", "z")
            every { mockCurrentUser.id.toString() } returns "1"
            every { CurrentUser.getCurrentUser() } returns mockCurrentUser
            coEvery { useCase.deleteUser("1", "2") } throws Exception()

            // When
           assertDoesNotThrow {   cli.show()}

            // Then
            verify (exactly = 1) {
                outputPrinter.printMenuItems(listOf(String.deleteUserStarted, String.enterUserId))


            }
        }
    }
}