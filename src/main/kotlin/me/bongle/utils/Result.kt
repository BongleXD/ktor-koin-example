package io.recho.utils

import com.fasterxml.jackson.annotation.JsonIgnore
import io.ktor.http.*

class FailResult<T>(
    val code: Int,
    val status: String,
    val message: String,
    val data: T?
) : Result<T> {

    override fun asStatusCode() = HttpStatusCode.fromValue(code)

    override fun <E> map(block: (T?) -> E) = FailResult(this.code, this.status, message, block(this.data))

}

class SuccessResult<T>(
    val code: Int,
    val status: String,
    val data: T?
) : Result<T> {

    override fun asStatusCode() = HttpStatusCode.fromValue(code)

    override fun <E> map(block: (T?) -> E) = SuccessResult(this.code, this.status, block(this.data))

}

interface Result<T> {

    @JsonIgnore
    fun asStatusCode(): HttpStatusCode

    fun <E> map(block: (T?) -> E): Result<E>

    companion object {

        fun <T> success(value: T? = null) = SuccessResult(HttpStatusCode.OK.value, "success", value)

        fun <T> failure(status: HttpStatusCode, message: String, value: T? = null) =
            FailResult(status.value, "fail", message, value)

        fun <T> error(status: HttpStatusCode, message: String, value: T? = null) =
            FailResult(status.value, "error", message, value)

    }

}