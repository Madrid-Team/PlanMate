package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.models.task.Task
import domain.models.task.TaskState
import domain.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class EditTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var editTaskUseCase: EditTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        editTaskUseCase = EditTaskUseCase(taskRepository)
    }

    @ParameterizedTest
    @CsvSource(
        "'title','',''",
        "'','2',''",
        "'','description',''",
        "'','','created by'",
    )
    fun `should return true when data provided is valid`(
        title: String,
        description: String,
        createdBy: String,
    ) {
        //given
        val oldTask = Task(
            id = "111",
            projectId = "11",
            title = "title",
            description = "description",
            state = TaskState.TODO,
            createdBy = "created by",
            logs = listOf()
        )

        val newTask = oldTask.copy(
            title = title.ifBlank { oldTask.title },
            description = description.ifBlank { oldTask.description },
            createdBy = createdBy.ifBlank { oldTask.createdBy }
        )
        every { taskRepository.getAllTasks() } returns listOf(oldTask)

        //when
        val result = editTaskUseCase.editTask(
            task = newTask
        )

        //then
        assertThat(result).isTrue()
    }
}