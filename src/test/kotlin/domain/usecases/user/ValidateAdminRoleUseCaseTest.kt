package domain.usecases.user


import domain.utils.UserExceptions
import domain.models.authentication.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ValidateAdminRoleUseCaseTest {

    private lateinit var validateAdminRoleuseCase: ValidateAdminRoleUseCase

    @BeforeEach
    fun setup() {
        validateAdminRoleuseCase = ValidateAdminRoleUseCase()
    }

    @Test
    fun `does not throw when role is ADMIN`() {
        validateAdminRoleuseCase.validate(UserRole.ADMIN.name)
    }

    @Test
    fun `throws UserNotAdminException when role is not ADMIN`() {
        assertThrows<UserExceptions.UserNotAdminException> {
            validateAdminRoleuseCase.validate(UserRole.MATE.name)
        }
    }
}

