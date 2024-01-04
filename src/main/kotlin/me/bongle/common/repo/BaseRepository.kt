package me.bongle.common.repo

import me.bongle.utils.ext.exposed.Page

/**
 * BaseRepository
 * @author Bongle
 * @time 2023/6/12 14:50
 */
interface BaseRepository<T> {

    fun findAll(size: Int, page: Int): Page<T>

}
