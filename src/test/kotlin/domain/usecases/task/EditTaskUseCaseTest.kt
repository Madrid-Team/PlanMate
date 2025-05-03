package domain.usecases.task

import domain.repository.TaskRepository
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class EditTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var editTaskUseCase: EditTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        editTaskUseCase = EditTaskUseCase(taskRepository)
    }

//    @ParameterizedTest
//    @CsvSource(
//        "'title','',''",
//        "'','2',''",
//        "'','description',''",
//        "'','','created by'",
//    )
//    fun `should return true when data provided is valid`(
//        title: String,
//        description: String,
//        createdBy: String,
//    ) {
//        //given
//        val oldTask = Task(
//            id = UUID.randomUUID(),
//            projectId = "11",
//            title = "title",
//            description = "description",
//            taskState = "TO Do",
//            createdBy = "created by",
//            logs = listOf()
//        )
//
//        val newTask = oldTask.copy(
//            title = title.ifBlank { oldTask.title },
//            description = description.ifBlank { oldTask.description },
//            createdBy = createdBy.ifBlank { oldTask.createdBy }
//        )
//        every { taskRepository.getAllTasks() } returns listOf(oldTask)
//        every { taskRepository.editTask(any()) } returns Result.success(Unit)
//
//
//        //when
//        val result = editTaskUseCase.editTask(
//            oldTaskId = oldTask.id.toString(),
//            updatedTask = newTask
//        )
//
//        //then
//        assertThat(result).isTrue()
//    }

//    @Test
//    fun `should return throw exception when id is not found`() {
//        //given
//        val task = createTask(
//            id = "12",
//            title = "title"
//        )
//        every { taskRepository.getAllTasks() } throws TaskNotFoundException()
//
//        assertThrows<TaskNotFoundException> {
//            editTaskUseCase.editTask(task.id.toString(), task)
//        }
}
