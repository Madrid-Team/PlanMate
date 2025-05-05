import data.source.project.ProjectDataSource
import data.source.user.UserDataSource
import data.utils.FileValidator
import di.modules.dataSourceModule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.File
import kotlin.test.assertNotNull

class FileValidatorTest : KoinTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setUp() {
            startKoin {
                modules(dataSourceModule)
            }
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            stopKoin()
        }
    }

    @Test
    fun `test userFileValidator is injected correctly`() {
        val userValidator: FileValidator by inject(named("userValidator"))
        assertNotNull(userValidator )
        assert(userValidator.file == File("user.csv"))
    }

    @Test
    fun `test taskFileValidator is injected correctly`() {
        val taskValidator: FileValidator by inject(named("taskValidator"))
        assertNotNull(taskValidator )
        assert(taskValidator.file == File("task.csv"))
    }


    @Test
    fun `test projectFileValidator is injected correctly`() {
        val projectValidator: FileValidator by inject(named("projectValidator"))
        assertNotNull(projectValidator)
        assert(projectValidator.file == File("project.csv"))
    }

    @Test
    fun `test ProjectDataSource is injected correctly`() {
        val projectDataSource: ProjectDataSource by inject()
        assertNotNull(projectDataSource)
    }


    @Test
    fun `test UserDataSource is injected correctly`() {
        val userDataSource: UserDataSource by inject()
        assertNotNull(userDataSource )
    }



}
