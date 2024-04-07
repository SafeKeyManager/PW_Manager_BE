package pw_manager.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import pw_manager.backend.entity.Member
import pw_manager.backend.repository.custom.MemberRepositoryCustom

interface MemberRepository : JpaRepository<Member, Long>, MemberRepositoryCustom{
    fun findByUserHash(username: String) : Member?
}
