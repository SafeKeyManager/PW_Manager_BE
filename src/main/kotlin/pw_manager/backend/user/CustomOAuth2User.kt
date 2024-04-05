package pw_manager.backend.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import pw_manager.backend.dto.response.OAuth2Response
import pw_manager.backend.entity.Member

class CustomOAuth2User(
    //private val oAuth2Response: OAuth2Response
        private val member: Member
) : OAuth2User {
    override fun getName(): String {
        return member.userHash
    }

    override fun getAttributes(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(
            GrantedAuthority { "USER" }
    )

}