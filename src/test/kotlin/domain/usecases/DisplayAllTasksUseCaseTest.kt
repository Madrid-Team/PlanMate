package domain.usecases

import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class DisplayAllTasksUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var taskRepository: TaskRepository
    private lateinit var displayAllTasksUseCase: DisplayAllTasksUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        taskRepository = mockk()
        displayAllTasksUseCase = DisplayAllTasksUseCase(projectRepository, taskRepository)
    }


}