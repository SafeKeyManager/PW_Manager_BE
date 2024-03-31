package pw_manager.backend.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pw_manager.backend.entity.Member
import pw_manager.backend.repository.MemberRepository

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
}
