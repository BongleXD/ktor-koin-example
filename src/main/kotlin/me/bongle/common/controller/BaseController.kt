package me.bongle.common.controller

import io.ktor.server.application.*
import io.ktor.server.auth.*
import me.bongle.modules.user.service.UserService
import me.bongle.plugins.UserPrincipal
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


/**
 * BaseController
 * @author Bongle
 * @time 2023/6/12 14:28
 */
open class BaseController : KoinComponent {

    private val service by inject<UserService>()

    fun ApplicationCall.isLogged() = safeGetLoginUser() != null

    fun ApplicationCall.getLoginUser() =
        safeGetLoginUser() ?: throw UnsupportedOperationException("未检测到用户登录凭据")

    fun ApplicationCall.safeGetLoginUser() =
        principal<UserPrincipal>()?.userId?.let {
            service.getUserById(it)
        }

}
