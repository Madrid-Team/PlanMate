package domain.usecases.user


import domain.utils.UserExceptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ValidatePasswordUseCaseTest {

    private lateinit var useCase: ValidatePasswordUseCase

    @BeforeEach
    fun setup() {
        useCase = ValidatePasswordUseCase()
    }

    @Test
    fun `throws EmptyPasswordException when password is empty`() {
        assertThrows<UserExceptions.EmptyPasswordException> {
            useCase.validatePassword("")
        }
    }

    @Test
    fun `throws PasswordLessThan6CharsException when password is too short`() {
        assertThrows<UserExceptions.PasswordLessThan6CharsException> {
            useCase.validatePassword("1234")
        }
    }

    @Test
    fun `does not throw exception for valid password`() {
        useCase.validatePassword("123456")
    }
}
