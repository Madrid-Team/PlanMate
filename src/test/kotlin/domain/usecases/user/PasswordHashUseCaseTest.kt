package domain.usecases.user

import org.junit.jupiter.api.Assertions.*

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PasswordHashUseCaseTest {

    private lateinit var passwordHashUseCase: PasswordHashUseCase

    @BeforeEach
    fun setup() {
        passwordHashUseCase = PasswordHashUseCase()
    }

    @Test
    fun `hashed password should not be equal to original password`() {
        val password = "123456"
        val hashed = passwordHashUseCase.passwordHash(password)

        assertThat(hashed).isNotEqualTo(password)
    }

    @Test
    fun `hashing the same password should return the same hash`() {
        val password = "mypassword"
        val hash1 = passwordHashUseCase.passwordHash(password)
        val hash2 = passwordHashUseCase.passwordHash(password)

        assertThat(hash1).isEqualTo(hash2)
    }
}
