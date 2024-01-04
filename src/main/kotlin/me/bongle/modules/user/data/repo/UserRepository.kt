package me.bongle.modules.user.data.repo

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.bongle.common.repo.BaseRepository
import me.bongle.modules.user.data.model.User
import me.bongle.modules.user.data.model.UserEntity
import me.bongle.modules.user.data.model.UserTable
import me.bongle.utils.ext.exposed.paginate
import me.bongle.utils.ext.exposed.safeFind
import me.bongle.utils.ext.exposed.safeSelectAll
import me.bongle.utils.ext.exposed.safeUpdate
import me.bongle.utils.validate.encrypt
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single

@Single
class UserRepository : BaseRepository<User> {

    fun create(name: String, email: String, password: String) = UserEntity.new {
        this.name = name
        this.email = email
        this.password = encrypt(password)
//        this.role = RoleEntity.safeFind { RoleTable.id eq "user" }.single()
        this.lastLoginIp = null
        this.lastLoginTime = null
    }.asDataObject()

    fun findById(id: Int) =
        transaction {
            UserEntity.safeFind { UserTable.id eq id }.singleOrNull()?.asDataObject()
        }

    fun findByEmailOrId(user: String, queryIp: String? = null) =
        transaction {
            UserEntity.safeFind { UserTable.email eq user or (UserTable.id eq user.toIntOrNull()) }.singleOrNull()
                ?.asDataObject()
        }

    fun updateLoginIp(user: User, address: String?) {
        transaction {
            UserTable.safeUpdate({ UserTable.id eq user.id }) {
                it[lastLoginIp] = address
            }
        }
    }

    fun updateLoginTime(user: User, time: Instant = Clock.System.now()) {
        transaction {
            UserTable.safeUpdate({ UserTable.id eq user.id }) {
                it[lastLoginTime] = time
            }
        }
    }

    override fun findAll(size: Int, page: Int) =
        transaction {
            UserTable.safeSelectAll().paginate(size, page) {
                UserEntity.wrapRow(it).asDataObject()
            }
        }

}
