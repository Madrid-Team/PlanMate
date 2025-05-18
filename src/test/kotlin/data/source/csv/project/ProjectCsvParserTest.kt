package data.source.csv.project

import com.google.common.truth.Truth.assertThat
import data.createProject
import data.dto.project.ProjectDto
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ProjectCsvParserTest {

    companion object {
        private lateinit var projectCsvParser: ProjectCsvParser
        private lateinit var project: ProjectDto
        private val row =
            "1,PlanMate,task management app,abc,project created|project updated,In progress,Todo|In progress|Done,Testing|Todo"

        @BeforeAll
        @JvmStatic
        fun initProject() {
            projectCsvParser = ProjectCsvParser()

            project = projectCsvParser.parseOneRowToProject(row)

        }
    }


    @Test
    fun `parseOneRowToProject function should parse project id correctly`() {
        assertThat(project.id).isEqualTo("1")
    }

    @Test
    fun `parseOneRowToProject function should parse project name correctly`() {
        assertThat(project.name).isEqualTo("PlanMate")
    }

    @Test
    fun `parseOneRowToProject function should parse project description correctly`() {
        assertThat(project.description).isEqualTo("task management app")
    }

    @Test
    fun `parseOneRowToProject function should parse project creator correctly`() {
        assertThat(project.createdBy).isEqualTo("abc")
    }

    @Test
    fun `parseOneRowToProject function should parse project logs correctly`() {
        assertThat(project.projectLogs).containsExactly(
            "project created",
            "project updated"
        )
    }

    @Test
    fun `parseOneRowToProject function should parse project state correctly`() {
        assertThat(project.projectState).isEqualTo("In progress")
    }


    @Test
    fun `parseOneRowToProject function should parse project task's states correctly`() {
        assertThat(project.taskStates).containsExactly(
            "Todo",
            "In progress",
            "Done"
        )
    }

    @Test
    fun `parseOneRowToProject function should parse project states correctly`() {
        assertThat(project.projectStates).containsExactly(
            "Testing",
            "Todo"
        )
    }

    @Test
    fun `parseProjectToString function should convert project object to valid row`() {

        val newProject = createProject(
            id = "2",
            name = "PlanMate",
            description = "project description",
            createdBy = "user2",
            projectState = "Todo",
            taskStates = listOf("Todo", "In progress"),
            projectStates = listOf("Testing", "Todo"),
            projectLogs = listOf("project created")
        )

        val newRow = projectCsvParser.parseProjectToString(newProject)
        val exceptedRow = "2,PlanMate,project description,user2,project created,Todo,Todo|In progress,Testing|Todo,\n"

        assertThat(newRow).isEqualTo(exceptedRow)
    }
}