package pw_manager.backend.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import pw_manager.backend.dto.response.OAuth2Response

class CustomOAuth2User(
    private val oAuth2Response: OAuth2Response
) : OAuth2User {
    override fun getName(): String {
        return oAuth2Response.getName()
    }

    override fun getAttributes(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(
            GrantedAuthority { "USER" }
    )

    fun getUsername(): String = "${oAuth2Response.getProvider()}_${oAuth2Response.getProviderId()}"


}