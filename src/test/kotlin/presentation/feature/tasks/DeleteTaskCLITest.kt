package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import domain.utils.TaskExceptions
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class DeleteTaskCLITest {
//    private lateinit var inputReader: InputReader
//    private lateinit var outputPrinter: OutputPrinter
//    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
//    private lateinit var deleteTaskCLI: DeleteTaskCLI
//    private lateinit var testScope: TestScope
//    private lateinit var coroutineScope: CoroutineScope
//
//    @BeforeEach
//    fun setUp() {
//        inputReader = mockk(relaxed = true)
//        outputPrinter = mockk(relaxed = true)
//        deleteTaskUseCase = mockk(relaxed = true)
//        coroutineScope = CoroutineScope(Dispatchers.IO)
//        deleteTaskCLI = DeleteTaskCLI(inputReader, outputPrinter, deleteTaskUseCase)
//        testScope = TestScope()
//    }
//
//    @Test
//    fun `should show success message when task is deleted successfully`() {
//        testScope.runTest {
//            // given
//            every { inputReader.readInput() } returnsMany listOf("1", "2")
//            coEvery { deleteTaskUseCase.deleteTask("1", "2") } returns Unit
//
//            // when
//            deleteTaskCLI.show()
//
//            // then
//            coVerify {
//                deleteTaskUseCase.deleteTask("1", "2")
//                outputPrinter.printMessage("=== Delete Task ===")
//                outputPrinter.printMessage("Enter task ID to delete:")
//                deleteTaskUseCase.deleteTask("1", "2")
//                outputPrinter.printMessage("Task deleted successfully.")
//            }
//        }
//    }
//
//    @Test
//    fun `should show fail message when task deletion fails`() {
//        // given
//        testScope.runTest {
//            every { inputReader.readInput() } returnsMany listOf("2", "1")
//            coEvery { deleteTaskUseCase.deleteTask("2", "1") } throws TaskExceptions.TaskCannotDeleteException()
//
//            // when
//            deleteTaskCLI.show()
//
//            // then
//            verify {
//                outputPrinter.printError(
//                    TaskExceptions.TaskCannotDeleteException().message ?: "Task not found or could not be deleted."
//                )
//            }
//        }
//    }
}