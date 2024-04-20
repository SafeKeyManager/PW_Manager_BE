package pw_manager.backend.repository.custom

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.util.StringUtils
import pw_manager.backend.dto.request.SearchDto
import pw_manager.backend.entity.QMember.*
import pw_manager.backend.entity.QSite
import pw_manager.backend.entity.QSite.*
import pw_manager.backend.entity.Site
import pw_manager.backend.entity.Site.SiteStatus.*
import java.util.function.LongSupplier

class SiteRepositoryCustomImpl(
        private val query: JPAQueryFactory
    ): SiteRepositoryCustom {
        override fun findSiteByUserHash(userHash: String, search: SearchDto, pageable: Pageable): Page<Site> {
            val contents = query
                .selectFrom(site)
                .join(site.member, member).fetchJoin()
                .where(
                    site.member.userHash.eq(userHash),
                    searchEq(search.search),
                    siteNotDelete()
                )
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

            val countQuery = query
                .select(site.count())
                .from(site)
                .leftJoin(site.member, member)
                .where(
                    site.member.userHash.eq(userHash),
                    searchEq(search.search),
                    siteNotDelete()
                )

            return PageableExecutionUtils.getPage(
                contents,
                pageable,
                LongSupplier { countQuery.fetchOne() ?: 0L }
            )
        }

    // TODO : 삭제처리 어떻게 할지
    private fun siteNotDelete(): BooleanExpression {
        return QSite.site.siteStatus.eq(DELETE).not()
    }

    private fun searchEq(search: String): BooleanExpression? {
        if(!StringUtils.hasText(search)){
            return null;
        }
        return site.siteName.contains(search)
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
