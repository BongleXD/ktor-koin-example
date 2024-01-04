package me.bongle.utils.ext.exposed

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow

data class Page<T>(
    val size: Int,
    val page: Int,
    val total: Long,
    val data: List<T>
)

/**
 * 分页函数
 *
 * @param page 页面数 (起始为 1)
 * @param size 总共页数
 * @param transform 将 ResultRow 映射成实体
 *
 * @return 分页后数据
 */
fun <T> Query.paginate(
    size: Int,
    page: Int,
    transform: (ResultRow) -> T
): Page<T> {
    val total = this.count()
    val data = this.limit(size, ((page - 1) * size.toLong())).toList()
    return Page(size, page, total, data.map(transform))
}
