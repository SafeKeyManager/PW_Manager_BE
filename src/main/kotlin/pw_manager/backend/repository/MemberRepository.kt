package pw_manager.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import pw_manager.backend.entity.Member

interface MemberRepository : JpaRepository<Member, Long>{
    fun findByuserHash(username: String) : Member?

}
