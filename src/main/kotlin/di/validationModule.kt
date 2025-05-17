package di

import domain.usecases.logs.LogsValidation
import domain.usecases.task.TaskValidatorUseCase
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
        TaskValidatorUseCase()
    }

}