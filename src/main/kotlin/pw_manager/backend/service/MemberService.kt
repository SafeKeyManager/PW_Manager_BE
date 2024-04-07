package pw_manager.backend.service

import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pw_manager.backend.entity.Member
import pw_manager.backend.repository.MemberRepository
import java.lang.RuntimeException

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MemberService (
    private val memberRepository: MemberRepository
){
    fun findAllMember():List<Member>{
        return memberRepository.findAll()
    }

    fun findMemberOnd():Member{
        memberRepository.findUserAndSites(SecurityContextHolder.getContext().authentication.name)
            ?.let{
                return it
            }
            // TODO : exception 추가하기
            ?: throw RuntimeException();
    }

    @Transactional
    fun saveFcmTokenToMember(fcmToken : String){

        val name : String = SecurityContextHolder.getContext().authentication.name

        val member = memberRepository.findByUserHash(name)
        if(member == null) {
            println("member is null")
            return
        }

        member.deviceToken = fcmToken
    }
}
