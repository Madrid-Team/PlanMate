package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.utils.PlanMateExceptions
import domain.validation.ValidateProjectName
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*

class EditProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var editProjectUseCase: EditProjectUseCase
    private lateinit var validateProjectName: ValidateProjectName
    private val testScope: TestScope = TestScope()

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        validateProjectName = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(projectRepository, validateProjectName)
    }

    @Test
    fun `editProject should return true when project is updated successfully in projectRepository`() {
        testScope.runTest {
            //Given
            val project = createProject(
                id = UUID.randomUUID().toString(),
                name = "Test Project",
                description = "dia"
            )
            coEvery { projectRepository.editProject(project) } returns Unit

            //When

            assertDoesNotThrow {
                editProjectUseCase(project)
            }
        }
    }

    @Test
    fun `editProject should return false when id is not found`() {
        testScope.runTest {
            //Given
            val project = createProject(
                id = UUID.randomUUID().toString(),
                name = "Test Project",
                description = "dia"
            )
            coEvery { projectRepository.editProject(project) } throws PlanMateExceptions("")

            //When
            assertThrows<PlanMateExceptions> {
                editProjectUseCase(project)
            }
        }
    }


    @Test
    fun `editProject should return false when updated name is invalid`() {
        testScope.runTest {
            //Given
            val project = createProject(
                name = "123&",
            )
            coEvery { projectRepository.editProject(project) } throws PlanMateExceptions("Can't edit task")
            //When & Then
            assertThrows<PlanMateExceptions> {
                editProjectUseCase(project)
            }
        }
    }

}