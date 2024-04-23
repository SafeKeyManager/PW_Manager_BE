package pw_manager.backend.repository.custom

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pw_manager.backend.dto.request.SearchDto
import pw_manager.backend.entity.Site

interface SiteRepositoryCustom {
    fun findSiteByUserHash(userHash: String, search: SearchDto, pageable: Pageable): Page<Site>

    fun findSiteByIdAndUserHashOrNull(siteId: Long, userHash: String): Site?

    fun findExpiredSiteAndUser(): List<Site>

}
