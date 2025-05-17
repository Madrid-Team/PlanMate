package data.source.csv.project

import com.google.common.truth.Truth.assertThat
import domain.usecases.createProject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*

class ProjectManagerTest {
    private lateinit var projectManager: ProjectManager

    @BeforeEach
    fun setUp() {
        projectManager = ProjectManager()
    }

    @Test
    fun `getProjects should return empty list initially`() {
        val projects = projectManager.getProjects()
        assertThat(projects).isEmpty()
    }


    @Test
    fun `getProjects should return list of projects`() {
        val projectId1 = UUID.randomUUID().toString()
        val projectId2 = UUID.randomUUID().toString()
        val projects = listOf(
            createProject(id = projectId1),
            createProject(id = projectId2)
        )
        assertThat(projectManager.getProjects())
    }

    @Test
    fun `setProjects should add multiple projects`() {
        val project1 = data.createProject()
        val project2 = data.createProject()
        projectManager.setProjects(listOf(project1, project2))
        val result = projectManager.getProjects()
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `addProject should add single project`() {
        val project = data.createProject()
        assertDoesNotThrow {
            projectManager.addProject(project)
        }
    }

    @Test
    fun `deleteProject should remove the correct project`() {
        val projectId1 = UUID.randomUUID().toString()
        val projectId2 = UUID.randomUUID().toString()
        val projects = listOf(
            data.createProject(id = projectId1),
            data.createProject(id = projectId2)
        )
        projectManager.setProjects(projects)
        val updatedProjects = projectManager.deleteProject(projectId1)
        assertThat(updatedProjects.size).isEqualTo(1)
    }

    @Test
    fun `editProject should update the correct project`() {
        val projectId1 = UUID.randomUUID().toString()
        val projectId2 = UUID.randomUUID().toString()
        val projects = listOf(
            data.createProject(id = projectId1, name = "Old project"),
            data.createProject(id = projectId2)
        )
        projectManager.setProjects(projects)

        val result = projectManager.editProject(projects[0].copy(name = "Updated project"))
        assertThat(result.find { it.id == projectId1 }!!.name).isEqualTo("Updated project")
    }

    @Test
    fun `addProject should add a new project to the list`() {
        val projectId1 = UUID.randomUUID().toString()
        val project = data.createProject(id = projectId1, name = "Added Project")
        projectManager.addProject(project)

        val result = projectManager.getProjects()
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id).isEqualTo(projectId1)
    }
}