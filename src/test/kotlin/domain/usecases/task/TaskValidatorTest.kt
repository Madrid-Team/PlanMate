package domain.usecases.task

import domain.utils.TaskDescriptionIsEmptyException
import domain.utils.TaskStateIsEmptyException
import domain.utils.TaskTitleIsEmptyException
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class TaskValidatorTest {
    private lateinit var taskValidator: TaskValidator
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskValidator = TaskValidator()
        testScope = TestScope()
    }

    @Test
    fun `validateBasic should pass when title and description are valid`() {
        testScope.runTest {
            val task = createTask(title = "Valid title", description = "Valid description")

            assertDoesNotThrow {
                taskValidator.validateBasic(task)
            }
        }
    }

    @Test
    fun `validateBasic should throw TaskTitleIsEmptyException when title is blank`() {
        testScope.runTest {
            val task = createTask(title = "")

            assertThrows<TaskTitleIsEmptyException> {
                taskValidator.validateBasic(task)
            }
        }
    }

    @Test
    fun `validateBasic should throw TaskDescriptionIsEmptyException when description is blank`() {
        testScope.runTest {
            val task = createTask(
                title = "Valid title",
                description = "",
                state = "TODO"
            )

            assertThrows<TaskDescriptionIsEmptyException> {
                taskValidator.validateBasic(task)
            }
        }
    }

    @Test
    fun `validateAll should pass when title, description and state are valid`() {
        testScope.runTest {
            val task = createTask(title = "Valid", description = "Valid", state = "TODO")

            assertDoesNotThrow {
                taskValidator.validateAll(task)
            }
        }
    }

    @Test
    fun `validateAll should throw TaskStateIsEmptyException when taskState is blank`() {
        testScope.runTest {
            val task = createTask(
                title = "Valid title",
                description = "Valid description",
                state = ""
            )

            assertThrows<TaskStateIsEmptyException> {
                taskValidator.validateAll(task)
            }
        }
    }
}