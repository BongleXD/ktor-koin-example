package me.bongle.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import io.ktor.server.application.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import me.bongle.config
import kotlin.time.Duration.Companion.days

private val jwtAudience = config.property("jwt.audience").getString()
private val jwtDomain = config.property("jwt.domain").getString()
private val jwtRealm = config.property("jwt.realm").getString()
private val jwtSecret = config.property("jwt.secret").getString()

fun createToken(userId: Int, duration: kotlin.time.Duration = 1.days) =
    JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim("userId", userId)
        .withExpiresAt(Clock.System.now().apply {
            plus(duration)
        }.toJavaInstant())
        .sign(Algorithm.HMAC256(jwtSecret))

fun Application.configureSecurity() {

    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience))
                    UserPrincipal(
                        credential.payload.getClaim("userId").asInt(),
                        credential.payload
                    ) else null
            }
        }
    }

}

class UserPrincipal(val userId: Int, payload: Payload) : Principal, JWTPayloadHolder(payload)
