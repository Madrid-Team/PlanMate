package presentation.feature.tasks

import data.mapper.toDto
import data.source.user.CurrentUserProvider
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.models.logs.CurrentUser
import domain.usecases.task.EditTaskUseCase
import io.mockk.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*
import kotlin.test.Test

class EditTaskCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var currentUserProvider: CurrentUserProvider
    private lateinit var taskViewer: TaskViewer
    private lateinit var editTaskCLI: EditTaskCLI
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        inputReader = mockk(relaxed = true)
        outputPrinter = mockk(relaxed = true)
        editTaskUseCase = mockk(relaxed = true)
        currentUserProvider = mockk()
        taskViewer = mockk(relaxed = true)
        editTaskCLI = EditTaskCLI(inputReader, outputPrinter, editTaskUseCase, currentUserProvider)
        testScope = TestScope()

        val testUser = User(id = UUID.randomUUID(), "hhdhdh", "sgsgsggs", UserRole.ADMIN.name)
        mockkObject(CurrentUser)
        every { currentUserProvider.getCurrentUser() } returns testUser.toDto()
    }


    @Test
    fun `should edit task successfully when call edit task`() {
        runTest {
            every { inputReader.readInput(any()) } returnsMany listOf(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "New Title",
                "New Description"
            )
            val updatedTask = helperTask(
                projectId = UUID.randomUUID().toString(),
                id = UUID.randomUUID().toString(),
                title = "New Title",
                description = "New Description"
            )
            coEvery { editTaskUseCase.editTask(updatedTask) } returns Unit

            editTaskCLI.show()

            verify { outputPrinter.printMessage("Task updated successfully") }
        }
    }
}