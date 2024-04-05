package pw_manager.backend.util


import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.SecureDigestAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JWTUtil(
        @Value("\${spring.jwt.secret}") secret: String
) {

    private val secretKey: SecretKey = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")

    fun getUsername(token: String) : String
    = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload["username"].toString()
    fun getRole(token: String) : String
    = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload["role"].toString()
    fun isExpired(token: String) : Boolean
    = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.expiration.before(Date())

    fun createJwt(username: String, role: String, expiredMs:Long) : String
    = Jwts.builder()
            .claim("username", username)
            .claim("role", role)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis()+expiredMs))
            .signWith(secretKey,SignatureAlgorithm.HS256)
            .compact()
}