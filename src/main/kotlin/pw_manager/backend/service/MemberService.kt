package pw_manager.backend.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import pw_manager.backend.entity.Member
import pw_manager.backend.repository.MemberRepository

@Service
@RequiredArgsConstructor
class MemberService (
    private val memberRepository: MemberRepository
){
    fun findAllMember():List<Member>{
        return memberRepository.findAll()
    }
}
