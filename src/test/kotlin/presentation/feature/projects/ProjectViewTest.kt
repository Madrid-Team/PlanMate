package presentation.feature.projects

import domain.models.project.Project
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import presentation.components.OutputPrinter
import java.util.*

class ProjectViewTest {

    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val projectView = ProjectView(outputPrinter)

    @Test
    fun `should print message when project list is empty`() {
        // Given
        val emptyProjects  = emptyList<Project>()

        // When
        projectView.projectList(emptyProjects)

        // Then
        verify { outputPrinter.printMessage("No projects to display.") }
    }

    @Test
    fun `should print project details when list has one project`() {
        // Given
        val project = Project(
            id = UUID.randomUUID(),
            name = "Test Project",
            description = "This is a test project.",
            projectState = "ACTIVE",
            createdBy = "admin",
            projectLogs = listOf("created", "updated"),
            projectStates = listOf("ACTIVE", "ARCHIVED"),
            taskStates = listOf("OPEN", "CLOSED")
        )

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
        val project1 = Project(UUID.randomUUID(), name = "Test Project 1", description = "This is a test project.", projectState = "OPEN", createdBy = "admin", projectLogs = listOf("created", "updated"), taskStates = listOf("OPEN", "CLOSED"), projectStates = listOf("OPEN", "CLOSED"))
        val project2 = Project(UUID.randomUUID(), name = "Test Project 2", description = "This is a test project.", projectState = "OPEN", createdBy = "admin", projectLogs = listOf("created", "updated"), taskStates = listOf("OPEN", "CLOSED"), projectStates = listOf("OPEN", "CLOSED"))

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
