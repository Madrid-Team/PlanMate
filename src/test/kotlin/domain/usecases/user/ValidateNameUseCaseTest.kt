package domain.usecases.user

import com.google.common.truth.Truth.assertThat

import domain.utils.NameValidationResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidateNameUseCaseTest {

    private lateinit var useCase: ValidateNameUseCase

    @BeforeEach
    fun setup() {
        useCase = ValidateNameUseCase()
    }

    @Test
    fun `should return NotValid if name is blank`() {
        val result = useCase.validateName("")
        assertThat(result).isInstanceOf(NameValidationResult.NotValid::class.java)
    }

    @Test
    fun `should return NotValid if name is less than 3 chars`() {
        val result = useCase.validateName("mo")
        assertThat(result).isInstanceOf(NameValidationResult.NotValid::class.java)
    }

    @Test
    fun `should return Valid if name is 3 or more chars`() {
        val result = useCase.validateName("mohamed")
        assertThat(result).isEqualTo(NameValidationResult.Valid)
    }
}
