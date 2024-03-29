package pw_manager.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pw_manager.backend.entity.Site

@Repository
interface SiteRepository : JpaRepository<Site, Long>{
    fun findBySiteNameContaining(siteName : String, pageable: Pageable) : Page<Site>
}
