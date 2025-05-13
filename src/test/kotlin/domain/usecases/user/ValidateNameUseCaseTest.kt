package domain.usecases.user

import domain.utils.UserExceptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ValidateNameUseCaseTest {

    private lateinit var useCase: ValidateNameUseCase

    @BeforeEach
    fun setup() {
        useCase = ValidateNameUseCase()
    }

    @Test
    fun `throws InvalidUserName when name is blank`() {
        assertThrows<UserExceptions.InvalidUserName> {
            useCase.validateName("")
        }
    }

    @Test
    fun `throws UserNameLessThan3CharsException when name is too short`() {
        assertThrows<UserExceptions.UserNameLessThan3CharsException> {
            useCase.validateName("ab")
        }
    }

    @Test
    fun `does not throw exception for valid name`() {
        useCase.validateName("mohamed")
    }
}

