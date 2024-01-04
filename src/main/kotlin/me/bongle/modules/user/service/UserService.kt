package me.bongle.modules.user.service

import me.bongle.modules.user.data.model.SafeUser
import me.bongle.modules.user.data.model.User
import me.bongle.modules.user.data.model.toSafeUser
import me.bongle.modules.user.data.pojo.LoginUser
import me.bongle.modules.user.data.repo.UserRepository
import me.bongle.utils.validate.validate
import org.koin.core.annotation.Single

interface UserService {

    fun loginUser(user: LoginUser): SafeUser?

    fun getUserById(userId: Int): User?

}

@Single
class UserServiceImpl(private val repo: UserRepository) : UserService {

   override fun loginUser(user: LoginUser): SafeUser? {
        // 用户是否存在
        val result = repo.findByEmailOrId(user.userId)
        // 验证密码是否正确
        if (result != null && validate(user.password, result.password)) {
            repo.updateLoginIp(result, user.address)
            repo.updateLoginTime(result)
            return result.toSafeUser()
        }
        // 返回登录用户
        return null
    }

    override fun getUserById(userId: Int): User? =
        repo.findById(userId)

}
