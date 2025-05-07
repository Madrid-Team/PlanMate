package domain.usecases.task

import domain.models.task.Task
import domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*
import kotlin.test.Test

class EditTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        editTaskUseCase = EditTaskUseCase(taskRepository)
        testScope = TestScope()
    }

    @ParameterizedTest
    @CsvSource(
        "'title','',''",
        "'','2',''",
        "'','description',''",
        "'','','created by'",
    )
    fun `editTask should execute successfully when repository not throw exception`(
        title: String,
        description: String,
        createdBy: String,
    ) {
       testScope.launch {
           //given
           val oldTask = Task(
               id = UUID.randomUUID(),
               projectId = "11",
               title = "title",
               description = "description",
               taskState = "TO Do",
               createdBy = "created by",
               logs = listOf()
           )

           val newTask = oldTask.copy(
               title = title.ifBlank { oldTask.title },
               description = description.ifBlank { oldTask.description },
               createdBy = createdBy.ifBlank { oldTask.createdBy }
           )

           coEvery { taskRepository.editTask(any()) } returns Unit

           assertDoesNotThrow { editTaskUseCase.editTask(newTask) }
       }
    }

    @Test
    fun `editTask should return throw exception when id is not found`() {
        //given
       testScope.launch {
           val task = createTask(
               id = UUID.randomUUID().toString(),
               title = "title"
           )

           assertThrows<Exception> {
               editTaskUseCase.editTask(task)
           }
       }
    }
}
