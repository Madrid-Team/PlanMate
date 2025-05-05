package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

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
        //Given
        val project =  createProject(
            id = UUID.randomUUID().toString(),
            name = "Test Project",
            description = "dia"
        )
        every { projectRepository.editProject(project) } returns Result.success(Unit)

        //When
        val result = editProjectUseCase.editProject(project)

        //Then
        assertTrue { result.isSuccess }
    }

    @Test
    fun `editProject should return false when id is not found`() {
        //Given
        val project =  createProject(
            id =UUID.randomUUID().toString(),
            name = "Test Project",
            description = "dia"
        )
        every { projectRepository.editProject(project) } returns Result.failure(PlanMateExceptions(""))

        //When
        val result = editProjectUseCase.editProject(project)

        //Then
        assertFalse { result.isSuccess }
    }

    @Test
    fun `editProject should return false when updated name is invalid`() {
        //Given
        val project =  createProject(
            name = "123&",
        )

        //When
        val result = editProjectUseCase.editProject(project)

        //Then
        assertFalse { result.isSuccess }
    }

}