package domain.usecases.user

import org.junit.jupiter.api.Assertions.*

import com.google.common.truth.Truth.assertThat
import domain.utils.PasswordValidationResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidatePasswordUseCaseTest {

    private lateinit var useCase: ValidatePasswordUseCase

    @BeforeEach
    fun setup() {
        useCase = ValidatePasswordUseCase()
    }

    @Test
    fun `should return NotValid if password is empty`() {
        val result = useCase.validatePassword("")
        assertThat(result).isInstanceOf(PasswordValidationResult.NotValid::class.java)
    }

    @Test
    fun `should return NotValid if password is less than 6 chars`() {
        val result = useCase.validatePassword("12345")
        assertThat(result).isInstanceOf(PasswordValidationResult.NotValid::class.java)
    }

    @Test
    fun `should return Valid if password is 6 or more chars`() {
        val result = useCase.validatePassword("123456")
        assertThat(result).isEqualTo(PasswordValidationResult.Valid)
    }
}
