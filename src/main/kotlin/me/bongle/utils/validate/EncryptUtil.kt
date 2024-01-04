package me.bongle.utils.validate

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("EncryptUtilKt")

fun validate(password: String, hashedPassword: String) = try {
    BCrypt.checkpw(password, hashedPassword)
} catch (ex: Throwable) {
    logger.error("Error on validate password: ${ex.message}")
    false
}

fun encrypt(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
