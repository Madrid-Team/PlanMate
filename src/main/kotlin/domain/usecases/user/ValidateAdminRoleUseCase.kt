package domain.usecases.user

import domain.models.authentication.UserRole

import domain.utils.AdminValidationResult


class ValidateAdminRoleUseCase() {
     fun validate( role: String): AdminValidationResult {
        return if (role == UserRole.ADMIN.name) {
            AdminValidationResult.Valid
        } else {
            AdminValidationResult.NotAdmin
        }
    }
}