package presentation.feature.projects

import data.source.csv.user.CurrentUserProvider
import domain.usecases.logs.AddAuditLogUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.deleteProjectException
import presentation.utils.deleteProjectHeader
import presentation.utils.deleteProjectSuccess
import presentation.utils.deletionCancelled
import presentation.utils.enterProjectIdToDelete
import presentation.utils.selectionOne
import presentation.utils.sureYouWantToDeleteThisProject
import kotlin.test.Test

class DeleteProjectCLITest {

    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val deleteProjectUseCase = mockk<DeleteProjectUseCase>()
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk<GetProjectByIdUseCase>(relaxed = true)
    private val addAuditLogUseCase: AddAuditLogUseCase = mockk<AddAuditLogUseCase>(relaxed = true)
    private val currentUserProvider: CurrentUserProvider = mockk<CurrentUserProvider>(relaxed = true)

    private lateinit var cli: DeleteProjectCLI

    @BeforeEach
    fun setUp() {
        cli = DeleteProjectCLI(
            inputReader,
            outputPrinter,
            deleteProjectUseCase,
            getProjectByIdUseCase,
            addAuditLogUseCase,
            currentUserProvider
        )
    }

    @Test
    fun `should delete project when user confirms`() = runTest {
        // Given
        val projectId = "project-123"

        every { inputReader.readInput(String.enterProjectIdToDelete) } returns projectId
        every { inputReader.readInput(String.sureYouWantToDeleteThisProject) } returns String.selectionOne
        coEvery { deleteProjectUseCase.deleteProjectByProjectId(projectId) } just runs

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage(String.deleteProjectHeader) }
        verify { outputPrinter.printMessage(String.deleteProjectSuccess) }

        coVerify { deleteProjectUseCase.deleteProjectByProjectId(projectId) }
    }

    @Test
    fun `should cancel deletion when user does not confirm`() = runTest {
        // Given
        val projectId = "project-123"
        every { inputReader.readInput(String.enterProjectIdToDelete) } returns projectId
        every { inputReader.readInput(String.sureYouWantToDeleteThisProject) } returns "no"

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage(String.deleteProjectHeader) }
        verify { outputPrinter.printMessage(String.deletionCancelled) }
        coVerify(exactly = 0) { deleteProjectUseCase.deleteProjectByProjectId(any()) }
    }

    @Test
    fun `should show error message when project not found`() = runTest {
        // Given
        val projectId = "invalid-id"
        val exception = ProjectExceptions.ProjectNotFoundException()
        every { inputReader.readInput(String.enterProjectIdToDelete) } returns projectId
        every { inputReader.readInput(String.sureYouWantToDeleteThisProject) } returns String.selectionOne
        coEvery { deleteProjectUseCase.deleteProjectByProjectId(projectId) } throws exception

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage(String.deleteProjectHeader) }
        verify { outputPrinter.printMessage(String.deleteProjectException.format(exception.message)) }
    }

    @Test
    fun `should show error message when other exception occurs`() = runTest {
        // Given
        val projectId = "error-id"
        val errorMessage = "Unexpected error"
        val exception = ProjectExceptions("Unexpected error")
        every { inputReader.readInput(String.enterProjectIdToDelete) } returns projectId
        every { inputReader.readInput(String.sureYouWantToDeleteThisProject) } returns String.selectionOne
        coEvery { deleteProjectUseCase.deleteProjectByProjectId(projectId) } throws exception

        // When
        cli.show()

        // Then
        verify { outputPrinter.printMessage(String.deleteProjectHeader) }
        verify { outputPrinter.printMessage(String.deleteProjectException.format(errorMessage)) }
    }
}