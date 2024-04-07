package pw_manager.backend.repository.custom

import pw_manager.backend.entity.Site

interface SiteRepositoryCustom {
    fun findSiteByUserHash(userHash: String): List<Site>

    fun findSiteByIdAndUserHashOrNull(siteId: Long, userHash: String): Site?

}
