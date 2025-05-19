package domain.usecases.user


import domain.models.authentication.UserRole
import domain.utils.UserNotAdminException
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
        assertThrows<UserNotAdminException> {
            validateAdminRoleuseCase.validate(UserRole.MATE.name)
        }
    }
}

