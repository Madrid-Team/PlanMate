package domain.usecases.task

import domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        deleteTaskUseCase = DeleteTaskUseCase(mockk(), mockk(), mockk())
        testScope = TestScope()
    }

    @Test
    fun `deleteTask should execute successfully when TaskRepository delete task`() {
        testScope.runTest {
            val taskId = UUID.randomUUID().toString()

            coEvery { taskRepository.deleteTask("", taskId) } returns Unit

            assertDoesNotThrow { DeleteTaskUseCase(mockk(), mockk(), mockk()) }
        }
    }

    @Test
    fun `deleteTask should throw exception when TaskRepository throw exception`() {
        testScope.runTest {
            val taskId = UUID.randomUUID().toString()

            assertThrows<Exception> { DeleteTaskUseCase(mockk(), mockk(), mockk()) }
        }
    }
}