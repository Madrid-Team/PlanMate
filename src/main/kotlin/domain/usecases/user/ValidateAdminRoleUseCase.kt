package domain.usecases.user

import domain.models.authentication.UserRole

import domain.utils.AdminValidationResult


class ValidateAdminRoleUseCase() {
     fun validate(role: String): Boolean {
         return role == UserRole.ADMIN.name
    }
}