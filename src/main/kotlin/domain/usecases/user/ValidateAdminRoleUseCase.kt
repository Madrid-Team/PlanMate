package domain.usecases.user


import domain.models.authentication.UserRole
import domain.utils.UserNotAdminException


class ValidateAdminRoleUseCase() {
     fun validate(role: String): Boolean {
         if (role != "ADMIN"){
           throw UserNotAdminException()
         }
         return role == UserRole.ADMIN.name
    }
}