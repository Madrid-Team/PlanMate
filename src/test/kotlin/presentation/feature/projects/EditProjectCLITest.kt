package presentation.feature.projects

import domain.models.authentication.User
import domain.models.logs.CurrentUser
import domain.usecases.project.*
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*
import kotlin.test.Test

class EditProjectCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val editProjectNameUseCase = mockk<EditProjectUseCase>()
//    private val editProjectDescriptionUseCase = mockk<EditProjectDescriptionUseCase>()
//    private val editProjectStateUseCase = mockk<EditProjectStateUseCase>()
    private val getProjectByIdUseCase = mockk<GetProjectByIdUseCase>()
    private val userMock = mockk<User>()

    private lateinit var cli: EditProjectCLI

    @BeforeEach
    fun setUp() {
        mockkObject(CurrentUser)
        every { userMock.username } returns "test-user"
        every { CurrentUser.getCurrentUser() } returns userMock
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns UUID.fromString("00000000-0000-0000-0000-000000000001")
        cli = EditProjectCLI(
            inputReader,
            outputPrinter,
            editProjectNameUseCase,
//            editProjectDescriptionUseCase,
//            editProjectStateUseCase,
            getProjectByIdUseCase
        )
    }

    @Test
    fun `should edit project name successfully`() {
        runTest {
            // Given
            val originalProject = helperProject(
                id = "00000000-0000-0000-0000-000000000001",
                name = "original-name",
                description = "original-description",
                createdBy = "test-user",
                projectState = "TODO"
            )

            every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
            every { inputReader.readInput("Select an option:") } returnsMany listOf("1", "4")
            every { inputReader.readInput("Enter the new name:") } returns "new-name"

            coEvery { getProjectByIdUseCase.getById("1") } returns originalProject
            coEvery {
//                editProjectNameUseCase.execute(originalProject.id, "new-name")
            } just runs

            // When
            cli.show()

            // Then
            verify { outputPrinter.printMessage("Project name updated successfully.") }
            verify { outputPrinter.printMessage("Project edited successfully.") }
//            coVerify { editProjectNameUseCase.execute(originalProject.id, "new-name") }
        }
    }

    @Test
    fun `should edit project description successfully`() {
        runTest {
            // Given
            val originalProject = helperProject(
                id = "00000000-0000-0000-0000-000000000001",
                name = "original-name",
                description = "original-description",
                createdBy = "test-user",
                projectState = "TODO"
            )

            every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
            every { inputReader.readInput("Select an option:") } returnsMany listOf("2", "4")
            every { inputReader.readInput("Enter the new description:") } returns "new-description"

            coEvery { getProjectByIdUseCase.getById("1") } returns originalProject
            coEvery {
//                editProjectDescriptionUseCase.execute(originalProject.id, "new-description")
            } just runs

            // When
            cli.show()

            // Then
            verify { outputPrinter.printMessage("Project description updated successfully.") }
            verify { outputPrinter.printMessage("Project edited successfully.") }
//            coVerify { editProjectDescriptionUseCase.execute(originalProject.id, "new-description") }
        }
    }

    @Test
    fun `should edit project state successfully`() {
        runTest {
            // Given
            val originalProject = helperProject(
                id = "00000000-0000-0000-0000-000000000001",
                name = "original-name",
                description = "original-description",
                createdBy = "test-user",
                projectState = "TODO",
                projectStates = listOf("TODO", "IN_PROGRESS", "DONE")
            )

            every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
            every { inputReader.readInput("Select an option:") } returnsMany listOf("3", "4")
            every { inputReader.readInput(match { it.contains("Select project State:") }) } returns "2"

            coEvery { getProjectByIdUseCase.getById("1") } returns originalProject
            coEvery {
//                editProjectStateUseCase.execute(originalProject.id, "IN_PROGRESS")
            } just runs

            // When
            cli.show()

            // Then
            verify { outputPrinter.printMessage("Project state updated successfully.") }
            verify { outputPrinter.printMessage("Project edited successfully.") }
//            coVerify { editProjectStateUseCase.execute(originalProject.id, "IN_PROGRESS") }
        }
    }

    @Test
    fun `should show error message when project not found`() {
        runTest {
            // Given
            every { inputReader.readInput("Enter the ID of the project to edit:") } returns "999"
            coEvery { getProjectByIdUseCase.getById("999") } throws ProjectExceptions.ProjectNotFoundException()

            // When & Then
            cli.show()
            verify { outputPrinter.printMessage(ProjectExceptions.ProjectNotFoundException().message!!) }
//            coVerify(exactly = 0) { editProjectNameUseCase.execute(any(), any()) }
//            coVerify(exactly = 0) { editProjectDescriptionUseCase.execute(any(), any()) }
//            coVerify(exactly = 0) { editProjectStateUseCase.execute(any(), any()) }
        }
    }

    @Test
    fun `should display error when name update fails with NoChangesException`() {
        runTest {
            // Given
            val originalProject = helperProject(
                id = "00000000-0000-0000-0000-000000000001",
                name = "original-name",
                description = "original-description",
                createdBy = "test-user",
                projectState = "TODO"
            )

            every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
            every { inputReader.readInput("Select an option:") } returnsMany listOf("1", "4")
            every { inputReader.readInput("Enter the new name:") } returns "original-name"

            coEvery { getProjectByIdUseCase.getById("1") } returns originalProject
            coEvery {
//                editProjectNameUseCase.execute(originalProject.id, "original-name")
            } throws ProjectExceptions.NoChangesException("Project name is unchanged")

            // When
            cli.show()

            // Then
            verify { outputPrinter.printMessage("Failed to update project name: Project name is unchanged") }
            verify { outputPrinter.printMessage("No changes were made to the project.") }
        }
    }

    @Test
    fun `should handle multiple successful updates`() {
        runTest {
            // Given
            val originalProject = helperProject(
                id = "00000000-0000-0000-0000-000000000001",
                name = "original-name",
                description = "original-description",
                createdBy = "test-user",
                projectState = "TODO",
                projectStates = listOf("TODO", "IN_PROGRESS", "DONE")
            )

            every { inputReader.readInput("Enter the ID of the project to edit:") } returns "1"
            every { inputReader.readInput("Select an option:") } returnsMany listOf("1", "2", "4")
            every { inputReader.readInput("Enter the new name:") } returns "new-name"
            every { inputReader.readInput("Enter the new description:") } returns "new-description"

            coEvery { getProjectByIdUseCase.getById("1") } returns originalProject
//            coEvery { editProjectNameUseCase.execute(originalProject.id, "new-name") } just runs
//            coEvery { editProjectDescriptionUseCase.execute(originalProject.id, "new-description") } just runs

            // When
            cli.show()

            // Then
            verify { outputPrinter.printMessage("Project name updated successfully.") }
            verify { outputPrinter.printMessage("Project description updated successfully.") }
            verify { outputPrinter.printMessage("Project edited successfully.") }
        }
    }
}