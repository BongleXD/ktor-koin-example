package me.bongle.utils.ext.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.jdbc.JdbcConnectionImpl
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun <T> Table.array(name: String, columnType: ColumnType): Column<List<T>> =
    registerColumn(name, ArrayColumnType(columnType))

class ArrayColumnType(private val type: ColumnType) : ColumnType() {
    override fun sqlType(): String = buildString {
        append(type.sqlType())
        append(" ARRAY")
    }

    override fun valueToDB(value: Any?): Any? = if (value is List<*>) {
        val columnType = type.sqlType().split("(")[0]
        val jdbcConnection = (TransactionManager.current().connection as JdbcConnectionImpl).connection
        jdbcConnection.createArrayOf(columnType, value.toTypedArray())
    } else {
        super.valueToDB(value)
    }

    override fun valueFromDB(value: Any): Any {
        return if (value is java.sql.Array) {
            val array = value.array
            if (array is Array<*>) {
                array.map {
                    if (it != null) {
                        type.valueFromDB(it)
                    } else null
                }.toList()
            } else {
                throw Exception("Values returned from database if not of type kotlin Array<*> ")
            }
        } else throw Exception("Values returned from database if not of type PgArray")
    }

    override fun notNullValueToDB(value: Any): Any {
        if (value is List<*>) {
            if (value.isEmpty())
                return "'{}'"

            val columnType = type.sqlType().split("(")[0]
            val jdbcConnection = (TransactionManager.current().connection as JdbcConnectionImpl).connection
            return jdbcConnection.createArrayOf(columnType, value.toTypedArray()) ?: error("Can't create non null array for $value")
        } else {
            return super.notNullValueToDB(value)
        }
    }
}

class AnyOp(val expr1: Expression<*>, val expr2: Expression<*>) : Op<Boolean>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        if (expr2 is OrOp) {
            queryBuilder.append("(").append(expr2).append(")")
        } else {
            queryBuilder.append(expr2)
        }
        queryBuilder.append(" = ANY (")
        if (expr1 is OrOp) {
            queryBuilder.append("(").append(expr1).append(")")
        } else {
            queryBuilder.append(expr1)
        }
        queryBuilder.append(")")
    }

}

class ContainsOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(expr1, expr2, "@>")

infix fun <T, S> ExpressionWithColumnType<T>.any(value: S): Op<Boolean> {
    if (value == null) {
        return IsNullOp(this)
    }
    return AnyOp(this, QueryParameter(value, columnType))
}

infix fun <T, S> ExpressionWithColumnType<T>.contains(array: Array<in S>): Op<Boolean> =
    ContainsOp(this, QueryParameter(array, columnType))