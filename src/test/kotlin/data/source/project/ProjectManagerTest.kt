//package data.source.project
//
//import com.google.common.truth.Truth.assertThat
//import domain.usecases.createProject
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertDoesNotThrow
//import java.util.*
//
//class ProjectManagerTest {
//
//
//    private lateinit var projectManager: ProjectManager
//
//    @BeforeEach
//    fun setUp() {
//        projectManager = ProjectManager()
//    }
//
//    @Test
//    fun `getProjects should return empty list initially`() {
//        val projects = projectManager.getProjects()
//        assertThat(projects).isEmpty()
//    }
//
//
//    @Test
//    fun `getProjects should return list of projects`() {
//        val projectId1 = UUID.randomUUID().toString()
//        val projectId2 = UUID.randomUUID().toString()
//        val projects = listOf(
//            createProject(id = projectId1),
//            createProject(id = projectId2)
//        )
////        projectManager.setProjects(projects)
//
//        assertThat(projectManager.getProjects()).isNotEmpty()
//        assertThat(projectManager.getProjects()).containsExactly(projects[0], projects[1])
//    }
//
//    @Test
//    fun `setProjects should add multiple projects`() {
//        val project1 = createProject()
//        val project2 = createProject()
//        projectManager.setProjects(listOf(project1, project2))
//        val result = projectManager.getProjects()
//        assertThat(result.size).isEqualTo(2)
//    }
//
//    @Test
//    fun `addProject should add single project`() {
//        val project = createProject()
//        assertDoesNotThrow {
//            projectManager.addProject(project)
//        }
//    }
//
//    @Test
//    fun `deleteProject should remove the correct project`() {
//        val projectId1 = UUID.randomUUID().toString()
//        val projectId2 = UUID.randomUUID().toString()
//        val projects = listOf(
//            createProject(id = projectId1),
//            createProject(id = projectId2)
//        )
//        projectManager.setProjects(projects)
//        val updatedProjects = projectManager.deleteProject(projectId1)
//        assertThat(updatedProjects.size).isEqualTo(1)
//    }
//
//    @Test
//    fun `editProject should update the correct project`() {
//        val projectId1 = UUID.randomUUID().toString()
//        val projectId2 = UUID.randomUUID().toString()
//        val projects = listOf(
//            createProject(id = projectId1, name = "Old project"),
//            createProject(id = projectId2)
//        )
//        projectManager.setProjects(projects)
//
//        val result = projectManager.editProject(projects[0].copy(name = "Updated project"))
//        assertThat(result.find { it.id.toString() == projectId1 }!!.name).isEqualTo("Updated project")
//    }
//
//    @Test
//    fun `addProject should add a new project to the list`() {
//        val projectId1 = UUID.randomUUID().toString()
//        val project = createProject(id = projectId1, name = "Added Project")
//        projectManager.addProject(project)
//
//        val result = projectManager.getProjects()
//        assertThat(result.size).isEqualTo(1)
//        assertThat(result[0].id.toString()).isEqualTo(projectId1)
//    }
//}