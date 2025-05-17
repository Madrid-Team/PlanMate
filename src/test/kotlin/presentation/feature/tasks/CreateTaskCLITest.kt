package presentation.feature.tasks

import data.source.csv.user.CurrentUserProvider
import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.task.TaskValidator
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.user.helperUser
import presentation.utils.*
import java.util.*

class CreateTaskCLITest {

    private val inputReader = mockk<InputReader>(relaxed = true)
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val createTaskUseCase = mockk<CreateTaskUseCase>(relaxed = true)
    private val getProjectByIdUseCase = mockk<GetProjectByIdUseCase>(relaxed = true)
    private val currentUserProvider = mockk<CurrentUserProvider>(relaxed = true)
    private val taskValidator = mockk<TaskValidator>(relaxed = true)
    private val taskRepository = mockk<TaskRepository>(relaxed = true)

    private lateinit var cli: CreateTaskCLI
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        cli = CreateTaskCLI(
            inputReader,
            outputPrinter,
            createTaskUseCase,
            getProjectByIdUseCase,
            currentUserProvider
        )
        testScope = TestScope()
    }

//    @Test
//    fun `should create task successfully when valid input is provided`() {
//        testScope.runTest {
//            // Given
//            val projectId = UUID.randomUUID()
//            val project = helperProject(id = projectId.toString(), taskStates = listOf("TODO", "IN_PROGRESS"))
//            val user = helperUser(username = "elhady")
//
//            coEvery { getProjectByIdUseCase.getById(projectId.toString()) } returns project
//            every { currentUserProvider.getCurrentUser() } returns user
//            every { inputReader.readInput(any()) } returnsMany listOf(
//                projectId.toString(),
//                "Title",
//                "Description",
//                "1"
//            )
//            every { taskValidator.validateAll(any()) } just Runs
//            coEvery { taskRepository.createTask(any()) } just Runs
//
//            // When
//            cli.show()
//
//            // Then
//            verify {
//                outputPrinter.printMessage(String.createTaskHeader)
//                outputPrinter.printMessage(String.taskCreatedSuccessfully)
//            }
//        }
//    }

    @Test
    fun `should show error message when project ID is not found and not create task`() {
        testScope.runTest {
            // Given
            val invalidProjectId = UUID.randomUUID().toString()
            val user = helperUser(username = "user")

            every { inputReader.readInput(any()) } returnsMany listOf(
                invalidProjectId,
                "title",
                "description"
            )
            every { currentUserProvider.getCurrentUser() } returns user
            coEvery { getProjectByIdUseCase.getProjectById(invalidProjectId) } throws ProjectExceptions.ProjectNotFoundException(
                "Project ID cannot be empty"
            )

            // When
            cli.show()

            // Then
            verifySequence {
                outputPrinter.printMessage(String.createTaskHeader)
                inputReader.readInput(String.enterProjectId)
                inputReader.readInput(String.enterTaskTitle)
                inputReader.readInput(String.enterTaskDescription)
                outputPrinter.printMessage(ProjectExceptions.ProjectNotFoundException("Project ID cannot be empty").message!!)
                outputPrinter.printMessage(String.taskCreatedUnsuccessfully)
            }
        }
    }

}

