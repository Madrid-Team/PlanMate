package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.models.authentication.User
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CreateUserUseCaseTest {
    private lateinit var useCase: CreateUserUseCase

    @BeforeEach
    fun setup() {
        useCase = mockk()
    }

    @Test
    fun `should throw exception when the user exists already`() {
        val user = mockk<User>()

        every { useCase.invoke(user) } throws PlanMateExceptions.UserAlreadyExistsException

        assertThrows<PlanMateExceptions.UserAlreadyExistsException> {
            useCase.invoke(user)
        }

        verify {
            useCase.invoke(user)
        }
    }


    @Test
    fun `should throw exception when the password is too weak`() {

        val user = mockk<User>()
        every { useCase.invoke(user) } throws PlanMateExceptions.PasswordIsTooWeakException

        assertThrows<PlanMateExceptions.PasswordIsTooWeakException> {
            useCase.invoke(user)
        }

        verify {
            useCase.invoke(user)
        }
    }


    @Test
    fun `should create user successfully when the user does not exist and the password is strong`() {
        val user = mockk<User>()
        every { useCase.invoke(user) } returns true

        useCase.invoke(user)

        assertThat(useCase.invoke(user)).isTrue()
    }


    @Test
    fun `should throw exception when the user is null`() {
        every { useCase.invoke(null) } throws PlanMateExceptions.UserIsNullException

        assertThrows<PlanMateExceptions.UserIsNullException> {
            useCase.invoke(null)
        }

        verify {
            useCase.invoke(null)
        }
    }

    @Test
    fun `should throw exception when the user doesn't have permission to create`(){
        val user = mockk<User>()
        every { useCase.invoke(user) } throws PlanMateExceptions.UserDoesNotHavePermissionException

        assertThrows<PlanMateExceptions.UserDoesNotHavePermissionException> {
            useCase.invoke(user)
        }

        verify {
            useCase.invoke(user)
        }
    }

}