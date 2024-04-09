package pw_manager.backend.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pw_manager.backend.dto.request.SearchDto
import pw_manager.backend.dto.request.SiteAddRequestDto
import pw_manager.backend.dto.request.SiteUpdateRequestDto
import pw_manager.backend.dto.response.SiteAddResponseDto
import pw_manager.backend.entity.Site
import pw_manager.backend.entity.Site.SiteStatus.*
import pw_manager.backend.exception.ErrorCode.*
import pw_manager.backend.exception.SiteException
import pw_manager.backend.repository.SiteRepository
import java.time.format.DateTimeFormatter.*

@Service
@Transactional(readOnly = true)
class SiteService (
    private val siteRepository: SiteRepository,
    private val memberService: MemberService
){
    // TODO : kotlin 엘비스 연산자로만 처리해보기
    fun getAllList(
        searchDto: SearchDto,
        pageable: Pageable
    ) : Page<SiteAddResponseDto>{
        val findSite =
            siteRepository.findBySiteNameContainingAndSiteStatusNot(searchDto.search, DELETE, pageable)
        if(findSite.isEmpty){
            throw SiteException(NOT_FOUND)
        }
        return findSite.map{
            it?.let {
                SiteAddResponseDto(
                    it.id!!, it.siteName, it.siteUrl, it.updateCycle.toString(),
                    it.createDate.format(ISO_DATE),
                    it.createDate.format(ISO_DATE)
                )
            }
        }
    }

    fun getAllMyList(
        searchDto: SearchDto,
        pageable: Pageable
    ): List<Site> {
        // TODO : search, pagination
        return siteRepository.findSiteByUserHash(SecurityContextHolder.getContext().authentication.name)
    }

    @Transactional
    fun addSite(request : SiteAddRequestDto): Site{
        val member = memberService.findMemberOnd()
        val saveSite = siteRepository.save(Site(member, request.siteName, request.siteUrl, request.siteCycle))
        // TODO : Entity 내부적으로 연관관계 처리하기
        member.sites.add(saveSite)
        return saveSite
    }

    @Transactional
    fun removeSite(siteId: Long): Long?{
        findSite(siteId)
            ?.let {
                it.siteStatus = DELETE
                return it.id
            }
            ?: throw SiteException(NOT_FOUND)
        // TODO : site ID 를 출력할수 있는 보조 생성자 만들기
        //?: throw NullPointerException("siteId = $siteId Not Found")
    }

    @Transactional
    fun updateCycle(siteId: Long): Long? {
        findSite(siteId)
            ?.let {
                it.updateDate = it.updateDate.plusDays(it.updateCycle)
                return it.id
            }
            ?: throw SiteException(NOT_FOUND)
    }

    @Transactional
    fun updateSiteInfo(siteId: Long, request: SiteUpdateRequestDto): Long? {
        findSite(siteId)?.let {
            it.updateSite(request)
            return it.id
        }
        ?: throw SiteException(NOT_FOUND)
    }

    fun findSite(siteId: Long): Site? {
        // TODO : 권한 체크를 해줘야 될까?
        return siteRepository.findSiteByIdAndUserHashOrNull(siteId, SecurityContextHolder.getContext().authentication.name)
    }
}
