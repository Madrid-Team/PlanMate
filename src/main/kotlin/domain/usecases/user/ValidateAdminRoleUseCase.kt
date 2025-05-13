package domain.usecases.user

import domain.models.authentication.UserRole

import domain.utils.AdminValidationResult
import domain.utils.UserExceptions


class ValidateAdminRoleUseCase() {
     fun validate(role: String): Boolean {
         if (role != "ADMIN"){
           throw UserExceptions.UserNotAdminException()
         }
         return role == UserRole.ADMIN.name
    }
}