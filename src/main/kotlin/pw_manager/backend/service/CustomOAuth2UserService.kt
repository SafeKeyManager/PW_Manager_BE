package pw_manager.backend.service

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import pw_manager.backend.dto.response.NaverResponse
import pw_manager.backend.entity.Member
import pw_manager.backend.repository.MemberRepository
import pw_manager.backend.user.CustomOAuth2User

@Service
class CustomOAuth2UserService(
        private val memberRepository: MemberRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        //userRequest가 null인지 체크
        val nonNullUserRequest = userRequest ?: throw OAuth2AuthenticationException("userRequest is null")
        println("accessToken : ${userRequest.accessToken.tokenValue}")
        val oAuth2User = super.loadUser(nonNullUserRequest)
        //println(oAuth2User.attributes)

        val registrationId = userRequest.clientRegistration.registrationId
        val oAuth2Response = when (registrationId){
            "naver" -> NaverResponse(oAuth2User.attributes)
            else -> throw OAuth2AuthenticationException("registrationId is not naver")
        }

        val userHash: String = "${oAuth2Response.getProvider()}_${oAuth2Response.getProviderId()}"
        var member = memberRepository.findByuserHash(userHash)

        if(member == null){
            member = Member(userHash)
            memberRepository.save(member)
        }

        return CustomOAuth2User(member)
    }
}