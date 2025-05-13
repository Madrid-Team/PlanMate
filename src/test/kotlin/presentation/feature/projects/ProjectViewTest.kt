package presentation.feature.projects

import domain.models.project.Project
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import presentation.components.OutputPrinter

class ProjectViewTest {

    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val projectView = ProjectView(outputPrinter)

    private val sampleLogs = listOf("created", "updated")

    @Test
    fun `should print message when project list is empty`() {
        // Given
        val emptyProjects = emptyList<Project>()

        // When
        projectView.projectList(emptyProjects)

        // Then
        verify { outputPrinter.printMessage("=== Projects List ===") }
    }

    @Test
    fun `should print project details when list has one project`() {
        // Given
        val project = helperProject(name = "Test Project", createdBy = "admin", projectLogs = sampleLogs)

        // When
        projectView.projectList(listOf(project))

        // Then
        verify { outputPrinter.printMessage("=== Projects List ===") }
        verify {
            outputPrinter.printMessage(
                match { it.contains("Test Project") && it.contains("admin") && it.contains("created") }
            )
        }
        verify { outputPrinter.printMessage("+--------------------------------------+") }
    }

    @Test
    fun `should print multiple projects correctly`() {
        // Given
        val project1 = helperProject(name = "Test Project 1", createdBy = "admin")
        val project2 = helperProject(name = "Test Project 2", createdBy = "admin")

        // When
        projectView.projectList(listOf(project1, project2))

        // Then
        verify { outputPrinter.printMessage("=== Projects List ===") }

        verify(exactly = 2) { outputPrinter.printMessage("+--------------------------------------+") }

        verify {
            outputPrinter.printMessage(
                match { it.contains("Test Project 1") && it.contains("admin") }
            )
        }

        verify {
            outputPrinter.printMessage(
                match { it.contains("Test Project 2") && it.contains("admin") }
            )
        }
    }
}
