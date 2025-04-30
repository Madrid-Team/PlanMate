package domain.utlis

sealed class PlanMateExceptions : Exception() {
    data object UserIsNullException : PlanMateExceptions()
    data object UserAlreadyExistsException : PlanMateExceptions() {
        private fun readResolve(): Any = UserAlreadyExistsException
    }

    data object PasswordIsTooWeakException : PlanMateExceptions()
    data object UserDoesNotHavePermissionException : PlanMateExceptions()
}