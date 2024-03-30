package pw_manager.backend.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pw_manager.backend.dto.request.SearchDto
import pw_manager.backend.dto.request.SiteAddRequestDto
import pw_manager.backend.dto.request.SiteUpdateRequestDto
import pw_manager.backend.dto.response.SiteAddResponseDto
import pw_manager.backend.entity.Site
import pw_manager.backend.entity.Site.SiteStatus.*
import pw_manager.backend.repository.SiteRepository
import java.time.format.DateTimeFormatter.*

@Service
@Transactional(readOnly = true)
class SiteService (
    private val siteRepository: SiteRepository
){
    fun getAllList(
        searchDto: SearchDto,
        pageable: Pageable
    ) : Page<SiteAddResponseDto>{
        return siteRepository.findBySiteNameContainingAndSiteStatusNot(searchDto.search, DELETE, pageable).map {
            SiteAddResponseDto(
                it.id!!, it.siteName, it.siteUrl, it.updateCycle.toString(),
                it.createDate.format(ISO_DATE),
                it.createDate.format(ISO_DATE)
            ) }
    }

    @Transactional
    fun addSite(request : SiteAddRequestDto) {
        siteRepository.save(Site(request.siteName, request.siteUrl, request.siteCycle))
    }

    @Transactional
    fun removeSite(siteId: Long) {
        // TODO : optional을 그냥 get으로 받아오지 말고 elseorThrorw 로 받아와서 예외 처리하기
        val findSite = siteRepository.findById(siteId).get()
        findSite.siteStatus = DELETE
    }

    @Transactional
    fun updateCycle(siteId: Long){
        // TODO : optional을 그냥 get으로 받아오지 말고 elseorThrorw 로 받아와서 예외 처리하기
        val findSite = siteRepository.findById(siteId).get()
        findSite.updateDate = findSite.updateDate.plusDays(findSite.updateCycle)
    }

    @Transactional
    fun updateSiteInfo(siteId: Long, request: SiteUpdateRequestDto): String {
        // TODO : optional을 그냥 get으로 받아오지 말고 elseorThrorw 로 받아와서 예외 처리하기
        val findSite = siteRepository.findById(siteId).get()
        findSite.updateSite(request)
        return "ok"
    }
}
