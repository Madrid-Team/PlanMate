package di.modules

import domain.usecases.logs.LogsValidation
import domain.usecases.task.TaskValidator
import domain.usecases.user.ValidateUser
import domain.utils.PasswordValidation
import org.koin.dsl.module

val validationModule = module {
    single {
        LogsValidation()
    }

    single {
        PasswordValidation()
    }

    single {
        ValidateUser(get())
    }

    single {
        TaskValidator()
    }

}