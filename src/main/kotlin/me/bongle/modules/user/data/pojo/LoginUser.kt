package me.bongle.modules.user.data.pojo

import org.jetbrains.annotations.Nullable

/**
 * 用户登录
 * @author Bongle
 * @time 2023/6/12 14:25
 */
data class LoginUser(
    val userId: String,
    val password: String,
    @Nullable var address: String? = null
)