package me.bongle.utils.ext.exposed

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

open class BaseIntIdTable(
    name: String,
) : BaseIdTable<Int>(name) {

    final override val id: Column<EntityID<Int>> = integer("id").autoIncrement().entityId()
    final override val primaryKey = PrimaryKey(id)

}

abstract class BaseIdTable<T : Comparable<T>>(
    name: String,
) : IdTable<T>(name) {

    abstract override val id: Column<EntityID<T>>
    abstract override val primaryKey: PrimaryKey
    open val insertTime = timestamp("insert_time").default(Clock.System.now())
    open val updateTime = timestamp("update_time").nullable()
    open val deleted = bool("deleted").default(false)

}

abstract class BaseIntEntity<T>(id: EntityID<Int>, table: BaseIdTable<Int>) : BaseEntity<Int, T>(id, table) {

    override var insertTime by table.insertTime
    override var updateTime by table.updateTime
    override var deleted by table.deleted

    abstract override fun asDataObject(): T

}

abstract class BaseEntity<ID : Comparable<ID>, E>(
    id: EntityID<ID>,
    table: BaseIdTable<ID>
) : Entity<ID>(id), IEntity<E> {

    override var insertTime by table.insertTime
    override var updateTime by table.updateTime
    override var deleted by table.deleted

    abstract override fun asDataObject(): E

}

open class BaseIntEntityClass<out E : BaseIntEntity<*>>(table: BaseIdTable<Int>) : BaseEntityClass<Int, E>(table)

open class BaseEntityClass<ID : Comparable<ID>, out E : Entity<ID>>(table: BaseIdTable<ID>) : EntityClass<ID, E>(table)

interface IEntity<T> {

    var insertTime: Instant
    var updateTime: Instant?
    var deleted: Boolean

    fun asDataObject(): T

}