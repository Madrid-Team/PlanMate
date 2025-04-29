package domain.usecases

import domain.repository.ProjectRepository
import domain.utlis.ProjectNotFoundException
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EditProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var editProjectUseCase: EditProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `editProject should throw ProjectNotFoundException when id is not found `() {
        //Given
        val project =  createProject(
            id = "40",
            name = "Test Project",
            description = "project contain two tasks"
        )

        //when & then
        assertThrows<ProjectNotFoundException> {
            editProjectUseCase.editProject(project)
        }


    }

    @Test
    fun `editProject should return true when updated name is valid`() {
        //Given
        val project =  createProject(
            name = "Test Project",
        )

        //When
        val result = editProjectUseCase.editProject(project)

        //Then
        assertTrue { result }
    }

    @Test
    fun `editProject should return false when updated name is invalid`() {
        //Given
        val project =  createProject(
            name = "$123&",
        )

        //When
        val result = editProjectUseCase.editProject(project)

        //Then
        assertFalse { result }
    }

    @Test
    fun `editProject should return true when updated description is valid`() {
        //Given
        val project =  createProject(
            description = "project contain two tasks",
        )

        //When
        val result = editProjectUseCase.editProject(project)

        //Then
        assertTrue { result }
    }

    @Test
    fun `editProject should return false when updated description is invalid`() {
        //Given
        val project =  createProject(
            description = "$123&",
        )

        //When
        val result = editProjectUseCase.editProject(project)

        //Then
        assertFalse { result }
    }

}