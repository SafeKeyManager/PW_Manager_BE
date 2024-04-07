package pw_manager.backend.repository.custom

import com.querydsl.jpa.impl.JPAQueryFactory
import pw_manager.backend.entity.Member
import pw_manager.backend.entity.QMember.*
import pw_manager.backend.entity.QSite.*

class MemberRepositoryImpl(
    private val query: JPAQueryFactory
):MemberRepositoryCustom {

    override fun findUserAndSites(userHash: String): Member? {
        return query
            .selectFrom(member)
            .where(member.userHash.eq(userHash))
            .join(member.sites, site).fetchJoin()
            .fetchOne()
    }
}
