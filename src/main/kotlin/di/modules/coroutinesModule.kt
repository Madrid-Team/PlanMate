package di.modules

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutineModule = module {
    single(named("Dispatcher IO")) { CoroutineScope(Dispatchers.IO) }
}