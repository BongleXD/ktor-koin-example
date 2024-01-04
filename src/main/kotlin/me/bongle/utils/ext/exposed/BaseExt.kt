package me.bongle.utils.ext.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.vendors.currentDialect

fun <ID : Comparable<ID>, E : BaseEntity<ID, *>> BaseEntityClass<out ID, E>.safeFind(
    op: SqlExpressionBuilder.() -> Op<Boolean>
) = this.find {
    this.op() and ((table as BaseIdTable<*>).deleted eq false)
}

fun <ID : Comparable<ID>, E : BaseEntity<ID, *>> BaseEntityClass<ID, E>.safeFindAll() =
    this.find {
        (table as BaseIdTable<*>).deleted eq false
    }

fun <E, T : BaseEntity<*, E>> SizedIterable<T>.toDataObjectList() = this.map { it.asDataObject() }.toList()

fun BaseIdTable<*>.safeSelectAll() = this.select { deleted eq false }

fun BaseIdTable<*>.safeSelect(where: SqlExpressionBuilder.() -> Op<Boolean>) =
    this.select { where() and (deleted eq false) }

fun <T : BaseIdTable<*>> T.safeUpdate(
    where: (SqlExpressionBuilder.() -> Op<Boolean>)? = null,
    limit: Int? = null,
    body: T.(UpdateStatement) -> Unit
): Int = this.update(
    { where?.let { it() and (deleted eq false) } ?: (deleted eq false) },
    limit
) {
    body(it)
    it[updateTime] = Clock.System.now()
}

class BaseVarCharColumnType<E : BaseEntity<String, *>>(
    clazz: BaseEntityClass<String, E>,
    val length: Int
) : BaseIdColumnType<String, E>(clazz) {

    override fun sqlType() = currentDialect.dataTypeProvider.varcharType(length)

}

class BaseIntColumnType<E : BaseEntity<Int, *>>(clazz: BaseEntityClass<Int, E>) : BaseIdColumnType<Int, E>(clazz) {

    override fun sqlType() = currentDialect.dataTypeProvider.integerType()

}

abstract class BaseIdColumnType<ID : Comparable<ID>, E : BaseEntity<ID, *>>(
    private val clazz: BaseEntityClass<ID, E>
) : ColumnType() {

    override fun valueFromDB(value: Any): Any {
        val id = value as? ID
        if (id != null) {
            return clazz.safeFind { clazz.table.id eq id }.single()
        }
        return super.valueFromDB(value)
    }

    override fun notNullValueToDB(value: Any): Any {
        val entity = value as E
        return entity.id.value
    }

    override fun valueToDB(value: Any?): Any? {
        val entity = value as? E
        if (entity != null) {
            return entity.id.value
        }
        return super.valueToDB(value)
    }

}