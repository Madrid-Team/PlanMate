package presentation.feature.user

import domain.models.authentication.User
import domain.models.logs.CurrentUser
import domain.usecases.user.DeleteUserUseCase
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteUserCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val useCase = mockk<DeleteUserUseCase>()
    private lateinit var cli: DeleteUserCLI

    @BeforeEach
    fun setUp() {
        cli = DeleteUserCLI(inputReader, outputPrinter, useCase)
        mockkObject(CurrentUser)
    }

    @Test
    fun `should delete user successfully when userId is exist`() {
        // Given
        val mockCurrentUser = mockk<User>()
        every { inputReader.readInput() } returns "2"
        every { mockCurrentUser.id.toString() } returns "1"
        every { CurrentUser.getCurrentUser() } returns mockCurrentUser
        coEvery { useCase.invoke("1", "2") } returns Unit

        // When
        cli.show()

        // Then
        verify {
            outputPrinter.printMessage("=== Delete user started ===")
            outputPrinter.printMessage("Enter user id:")
            outputPrinter.printMessage("Deleted Success")
        }
        verify(exactly = 0) { outputPrinter.printMessage("Deleted Failed") }
    }

    @Test
    fun `should show error message when delete user fails`() {
        // Given
        val mockCurrentUser = mockk<User>()
        every { inputReader.readInput() } returnsMany listOf("2", "z")
        every { mockCurrentUser.id.toString() } returns "1"
        every { CurrentUser.getCurrentUser() } returns mockCurrentUser
        coEvery { useCase.invoke("1", "2") } throws Exception()

        // When
        cli.show()

        // Then
        verify {
            outputPrinter.printMessage("=== Delete user started ===")
            outputPrinter.printMessage("Enter user id:")
            outputPrinter.printMessage("Deleted Failed")
            outputPrinter.printMessage("if you want to try again enter \"1\" else enter anything")
        }
    }


}