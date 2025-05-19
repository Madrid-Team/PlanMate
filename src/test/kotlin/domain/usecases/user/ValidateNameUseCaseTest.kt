package domain.usecases.user

import domain.utils.InvalidUserName
import domain.utils.UserNameLessThan3CharsException
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
        assertThrows<InvalidUserName> {
            useCase.validateName("")
        }
    }

    @Test
    fun `throws UserNameLessThan3CharsException when name is too short`() {
        assertThrows<UserNameLessThan3CharsException> {
            useCase.validateName("ab")
        }
    }

    @Test
    fun `does not throw exception for valid name`() {
        useCase.validateName("mohamed")
    }
}

