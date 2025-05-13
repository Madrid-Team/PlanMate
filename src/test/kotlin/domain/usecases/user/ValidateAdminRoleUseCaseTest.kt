package domain.usecases.user

import org.junit.jupiter.api.Assertions.*

import com.google.common.truth.Truth.assertThat
import domain.models.authentication.UserRole
import domain.utils.AdminValidationResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidateAdminRoleUseCaseTest {

    private lateinit var useCase: ValidateAdminRoleUseCase

    @BeforeEach
    fun setup() {
        useCase = ValidateAdminRoleUseCase()
    }

    @Test
    fun `should return Valid if role is ADMIN`() {
        val result = useCase.validate(UserRole.ADMIN.name)
        assertThat(result).isEqualTo(AdminValidationResult.Valid)
    }

    @Test
    fun `should return NotAdmin if role is not ADMIN`() {
        val result = useCase.validate(UserRole.MATE.name)
        assertThat(result).isEqualTo(AdminValidationResult.NotAdmin)
    }
}
