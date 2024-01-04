package me.bongle.modules.user.data.model

import kotlinx.datetime.Instant
import me.bongle.utils.ext.exposed.BaseIntEntity
import me.bongle.utils.ext.exposed.BaseIntEntityClass
import me.bongle.utils.ext.exposed.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UserTable : BaseIntIdTable("user_t") {

    val name = varchar("name", 50)
    val email = varchar("email", 50).uniqueIndex()
    val password = varchar("password", 200)
//    val role = reference("role_id", RoleTable)
    val lastLoginIp = varchar("last_login_ip", 50).nullable()
    val lastLoginTime = timestamp("last_login_time").nullable()

}

class UserEntity(id: EntityID<Int>) : BaseIntEntity<User>(id, UserTable) {

    var name by UserTable.name
    var email by UserTable.email
    var password by UserTable.password
//    var role by RoleEntity referencedOn UserTable.role
    var lastLoginIp by UserTable.lastLoginIp
    var lastLoginTime by UserTable.lastLoginTime

    companion object : BaseIntEntityClass<UserEntity>(UserTable)

    override fun asDataObject() =
        User(id.value, name, email, password, lastLoginIp, insertTime, lastLoginTime)

}

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
//    val role: Role,
    val lastLoginIp: String?,
    val registerTime: Instant,
    val lastLoginTime: Instant?
)

fun User.toSafeUser() =
    SafeUser(id, name, registerTime.toEpochMilliseconds(), lastLoginTime?.toEpochMilliseconds())

data class SafeUser(
    val id: Int,
    val name: String,
//    @JsonIgnore
//    val role: Role,
    val registerTime: Long,
    val lastLoginTime: Long?
)

fun SafeUser.getLastLoginTime() = lastLoginTime?.let { Instant.fromEpochMilliseconds(it) }
fun SafeUser.getLastRegisterTime() = Instant.fromEpochMilliseconds(registerTime)