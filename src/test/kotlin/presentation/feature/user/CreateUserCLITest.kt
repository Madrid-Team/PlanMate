package presentation.feature.user

import domain.usecases.user.CreateUserUseCase
import domain.utils.PasswordLessThan6CharsException
import domain.utils.UserExist
import domain.utils.UserReadWriteException
import io.mockk.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.enterUserName
import java.lang.reflect.Method
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext

class CreateUserCLITest {
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val useCase = mockk<CreateUserUseCase>()
    private lateinit var cli: CreateUserCLI

    private lateinit var tryCreateUserMethod: Method
    private lateinit var handleUserReadWriteErrorMethod: Method
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        cli = CreateUserCLI(useCase, inputReader, outputPrinter)
        testScope = TestScope()

        tryCreateUserMethod = cli::class.java.getDeclaredMethod(
            "tryCreateUser",
            String::class.java,
            String::class.java,
            Continuation::class.java
        ).apply { isAccessible = true }

        handleUserReadWriteErrorMethod = cli::class.java.getDeclaredMethod(
            "handleUserReadWriteError",
            String::class.java,
            String::class.java,
            UserReadWriteException::class.java,
            Continuation::class.java
        ).apply { isAccessible = true }

    }

    private fun continuation(): Continuation<Unit> = object : Continuation<Unit> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<Unit>) {}
    }


    @Test
    fun `test tryCreateUser directly - successful case`() = testScope.runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        coEvery { useCase.createUser(username, password) } returns Unit

        // When - invoke private method directly
        tryCreateUserMethod.invoke(cli, username, password, continuation())
        // Then
        coVerify { useCase.createUser(username, password) }
        verify {
            outputPrinter.printMessage("Creating user...")
            outputPrinter.printMessage("User created successfully")
        }
    }

    @Test
    fun `test tryCreateUser directly - UserReadWriteException case`() = testScope.runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        val errorMessage = "User read write error"
        val exception = UserReadWriteException("User read write error")

        coEvery { useCase.createUser(username, password) } throws exception
        every { inputReader.readInput() } returns "3" // any key other than 1 or 2 to exit

        // When - invoke private method directly
        tryCreateUserMethod.invoke(cli, username, password,continuation())

        // Then
        coVerify { useCase.createUser(username, password) }
        verify {
            outputPrinter.printMessage("Creating user...")
            outputPrinter.printError(errorMessage)
            outputPrinter.printMessage("1 - Try again with same data\n2 - Enter new data\nAny other key - Exit")
        }
    }

    @Test
    fun `test handleUserReadWriteError directly - retry with same data`() = runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        val exception = UserReadWriteException("Failed to write user data")

        every { inputReader.readInput() } returns "1"
        coEvery { useCase.createUser(username, password) } returns Unit

        handleUserReadWriteErrorMethod.invoke(cli, username, password, exception,continuation())

        // Then
        verify {
            outputPrinter.printError("Failed to write user data")
            outputPrinter.printMessage("1 - Try again with same data\n2 - Enter new data\nAny other key - Exit")
        }
    }

    @Test
    fun `test handleUserReadWriteError directly - enter new data`() = runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        val newUsername = "newuser"
        val newPassword = "newpass"
        val exception = UserReadWriteException("Failed to write user data")

        every { inputReader.readInput() } returns "2" // option 2 - enter new data
        every { inputReader.readInput(String.enterUserName) } returns newUsername
        every { inputReader.readInput("Enter password (minimum 6 characters):") } returns newPassword
        coEvery { useCase.createUser(newUsername, newPassword) } returns Unit

        handleUserReadWriteErrorMethod.invoke(cli, username, password, exception,continuation())

        // Then
        verify {
            outputPrinter.printError("Failed to write user data")
            outputPrinter.printMessage("1 - Try again with same data\n2 - Enter new data\nAny other key - Exit")
            outputPrinter.printMessage("=== Create user started ===")
        }
    }

    @Test
    fun `should create user success when user name and password is correct`() =
        testScope.runTest {
            // Given
            val username = "admin"
            val password = "123456"

            every { inputReader.readInput(String.enterUserName) } returns username
            every { inputReader.readInput("Enter password (minimum 6 characters):") } returns password
            coEvery { useCase.createUser(username,password) } returns Unit
            // When
            cli.show()

            // Then
            verify {
                outputPrinter.printMessage("=== Create user started ===")
                outputPrinter.printMessage("Creating user...")
                outputPrinter.printMessage("User created successfully")
            }
            verify(exactly = 0) { outputPrinter.printError(any()) }
        }


    @Test
    fun `should show error message when username already exists`() = testScope.runTest {
        // Given
        val username = "admin"
        val password = "123456"
        val errorMessage = "User already Exist"

        every { inputReader.readInput(String.enterUserName) } returns username
        every { inputReader.readInput("Enter password (minimum 6 characters):") } returns password
        every { inputReader.readInput() } returns "2"
        coEvery { useCase.createUser(username, password) } throws UserExist("User already Exist")

        // When
        cli.show()

        // Then
        coVerify { useCase.createUser(username, password) }
        verify {
            outputPrinter.printMessage("=== Create user started ===")
            outputPrinter.printMessage("Creating user...")
            outputPrinter.printError("Error: $errorMessage")
            outputPrinter.printMessage("Enter 1 to try again or any other key to exit")
        }
    }


    @Test
    fun `should show error message and exit when password is less that 6 digits`() = testScope.runTest {
        // Given
        val username = "admin"
        val password = "123"
        val errorMessage = "Password cannot be less than 6 chars length"

        every { inputReader.readInput(String.enterUserName) } returns username
        every { inputReader.readInput("Enter password (minimum 6 characters):") } returns password
        every { inputReader.readInput() } returns "2"
        coEvery { useCase.createUser(username, password) } throws PasswordLessThan6CharsException("Password cannot be less than 6 chars length")

        // When
        cli.show()

        // Then
        coVerify { useCase.createUser(username, password) }
        verify {
            outputPrinter.printMessage("=== Create user started ===")
            outputPrinter.printMessage("Creating user...")
            outputPrinter.printError("Error: $errorMessage")
            outputPrinter.printMessage("Enter 1 to try again or any other key to exit")
        }
    }
}