package pw_manager.backend.service

import lombok.RequiredArgsConstructor
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pw_manager.backend.entity.Member
import pw_manager.backend.repository.MemberRepository
import pw_manager.backend.user.CustomOAuth2User

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MemberService (
    private val memberRepository: MemberRepository
){
    fun findAllMember():List<Member>{
        return memberRepository.findAll()
    }

    @Transactional
    fun saveMember(member: Member): Member{
        return memberRepository.save(member)
    }

    @Transactional
    fun saveFcmTokenToMember(fcmToken : String){

        val name : String = SecurityContextHolder.getContext().authentication.name

        val member = memberRepository.findByuserHash(name)
        if(member == null) {
            println("member is null")
            return
        }

        member.deviceToken = fcmToken
    }
}
