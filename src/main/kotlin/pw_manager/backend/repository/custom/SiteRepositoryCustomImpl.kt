package pw_manager.backend.repository.custom

import com.querydsl.jpa.impl.JPAQueryFactory
import pw_manager.backend.entity.QMember.*
import pw_manager.backend.entity.QSite.*
import pw_manager.backend.entity.Site

class SiteRepositoryCustomImpl(
    private val query: JPAQueryFactory
): SiteRepositoryCustom {
    // TODO : pagination 적용
    override fun findSiteByUserHash(userHash: String): List<Site> {
        return query
            .selectFrom(site)
            .join(site.member, member).fetchJoin()
            .where(member.userHash.eq(userHash))
            .fetch()
    }

    override fun findSiteByIdAndUserHashOrNull(siteId: Long, userHash: String): Site? {
        return query
            .selectFrom(site)
            .join(site.member, member).fetchJoin()
            .where(member.userHash.eq(userHash),
                site.id.eq(siteId))
            .fetchOne()
    }
}
