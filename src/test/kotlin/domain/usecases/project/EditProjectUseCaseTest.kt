package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectExceptions
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*

class EditProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var editProjectUseCase: EditProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `editProject should return true when project is updated successfully in projectRepository`() {
        runTest {
            //Given
            val project = createProject(
                id = UUID.randomUUID().toString(),
                name = "Test Project",
                description = "dia"
            )
            coEvery { projectRepository.editProject(project) } returns Unit

            //When

            assertDoesNotThrow {
                editProjectUseCase.editProject(project)
            }
        }
    }

    @Test
    fun `editProject should return false when id is not found`() {
        runTest {
            //Given
            val project = createProject(
                id = UUID.randomUUID().toString(),
                name = "Test Project",
                description = "dia"
            )
            coEvery { projectRepository.editProject(project) } throws PlanMateExceptions("")

            //When
            assertThrows<PlanMateExceptions> {
                editProjectUseCase.editProject(project)
            }
        }
    }


    @Test
    fun `editProject should return false when updated name is invalid`() {
        runTest {
            //Given
            val project = createProject(
                name = "123&",
            )
            //When & Then
            assertThrows<ProjectExceptions.ProjectNameInvalidException> {
                editProjectUseCase.editProject(project)
            }
        }
    }

}