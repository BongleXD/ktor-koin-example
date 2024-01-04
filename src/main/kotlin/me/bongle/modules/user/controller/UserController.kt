package me.bongle.modules.user.controller

import com.github.dimitark.ktorannotations.annotations.Post
import com.github.dimitark.ktorannotations.annotations.RouteController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.bongle.common.controller.BaseController
import me.bongle.ksp.slf4j.Slf4j
import me.bongle.modules.user.data.pojo.LoginUser
import me.bongle.modules.user.service.UserService
import me.bongle.plugins.createToken
import me.bongle.utils.ext.failure
import me.bongle.utils.ext.host
import me.bongle.utils.ext.success
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.days

@RouteController
@Slf4j
class UserController : BaseController() {

    private val service by inject<UserService>()

    @Post("/login")
    suspend fun login(call: ApplicationCall) {
        // 已登录则不进行登录
        if (call.isLogged()) {
            call.failure(HttpStatusCode.OK, "用户已登录")
            return
        }
        // 校验用户邮箱及密码
        val user = call.receive<LoginUser>()
        user.address = call.request.host
        val loggedUser = service.loginUser(user)
        if (loggedUser != null) {
            // 登录成功
            call.success(mapOf("token" to createToken(loggedUser.id, 1.days)))
            return
        }
        // 登录失败
        call.failure(HttpStatusCode.OK, "用户名或密码错误")
    }

    @Post("/register")
    suspend fun register(call: ApplicationCall) {
        logger.info("Register!")
    }

}
