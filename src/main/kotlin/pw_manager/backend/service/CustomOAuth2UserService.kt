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

        val oAuth2User = super.loadUser(nonNullUserRequest)
        println(oAuth2User.attributes)

        val registrationId = userRequest.clientRegistration.registrationId
        val oAuth2Response = when (registrationId){
            "naver" -> NaverResponse(oAuth2User.attributes)
            else -> throw OAuth2AuthenticationException("registrationId is not naver")
        }

        val username: String = "${oAuth2Response.getProvider()}_${oAuth2Response.getProviderId()}"
        var member = memberRepository.findByuserHash(username)

        if(member == null){
            member = Member(username, oAuth2Response.getEmail())
            memberRepository.save(member)
        }
        else{
            //Todo : 현재는 email만 업데이트 하지만 username도 업데이트 해야할 필요성이 있다면 추가 수정
            member.email = oAuth2Response.getEmail()
        }

        //Todo : role을 Member에 추가시켜야 하는가(해야겠지..?)
        val role = "USER"
        return CustomOAuth2User(oAuth2Response, role)
    }
}