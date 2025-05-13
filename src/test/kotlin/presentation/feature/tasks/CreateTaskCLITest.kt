package presentation.feature.tasks

import domain.usecases.project.GetProjectByIdUseCase
import domain.usecases.task.CreateTaskUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.helperProject
import java.util.*

//class CreateTaskCLITest {
//    private val inputReader = mockk<InputReader>(relaxed = true)
//    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
//    private val createTaskUseCase = mockk<CreateTaskUseCase>(relaxed = true)
//    private val getProjectByIdUseCase = mockk<GetProjectByIdUseCase>(relaxed = true)
//    private lateinit var cli: CreateTaskCLI
//    private lateinit var testScope: TestScope
//
//    @BeforeEach
//    fun setUp() {
//        cli = CreateTaskCLI(
//            inputReader,
//            outputPrinter,
//            createTaskUseCase,
//             getProjectByIdUseCase
//        )
//        testScope = TestScope()
//    }
//
//    @Test
//    fun `should create task successfully when valid input is provided`() {
//        testScope.runTest {
//            // Given
//            every { inputReader.readInput(any()) } returnsMany listOf(
//                UUID.randomUUID().toString(),
//                "title",
//                "description"
//            )
//            val project = helperProject(id = UUID.randomUUID().toString())
//            val task = helperTask(projectId = project.id.toString(), title = "title", description = "description")
//            coEvery { createTaskUseCase.createTask(task) } returns Unit
//
//            // When
//            cli.show()
//
//            // Then
////        verify { createTaskUseCase.createTask(task) }
//            verify { outputPrinter.printMessage(any()) }
//        }
//    }
//
//    @Test
//    fun `should show error message when project ID is not found and not create task`() {
//        // Given
//        testScope.runTest {
//            every { inputReader.readInput(any()) } returnsMany listOf("invalid_id", "title", "description")
//            val task = helperTask(projectId = "invalid_id", title = "title", description = "description")
//            coEvery { createTaskUseCase.createTask(task) } returns Unit
//
//            // When
//            cli.show()
//
//            // Then
//            verify { outputPrinter.printMessage("Failed to create task") }
//        }
//    }
//}