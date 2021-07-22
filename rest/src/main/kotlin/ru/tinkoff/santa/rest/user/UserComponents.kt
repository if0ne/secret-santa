package ru.tinkoff.santa.rest.user

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.tinkoff.santa.rest.user.authentication.AuthenticationController
import ru.tinkoff.santa.rest.user.registration.RegistrationController

fun DI.Builder.userComponents() {
    bind<UserDao>() with singleton { UserDao(instance()) }
    bind<UserService>() with singleton { UserService(instance()) }
    bind<AuthenticationController>() with singleton { AuthenticationController(instance()) }
    bind<RegistrationController>() with singleton { RegistrationController(instance()) }
    bind<UserController>() with singleton { UserController(instance(), instance(), instance(), instance()) }
}