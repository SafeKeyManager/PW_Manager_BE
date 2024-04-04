package pw_manager.backend.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pw_manager.backend.dto.request.SearchDto
import pw_manager.backend.dto.request.SiteAddRequestDto
import pw_manager.backend.dto.request.SiteUpdateRequestDto
import pw_manager.backend.dto.response.SiteAddResponseDto
import pw_manager.backend.entity.Member
import pw_manager.backend.entity.Site
import pw_manager.backend.entity.Site.SiteStatus.*
import pw_manager.backend.exception.ErrorCode.*
import pw_manager.backend.exception.SiteException
import pw_manager.backend.repository.SiteRepository
import pw_manager.backend.user.CustomOAuth2User
import java.time.format.DateTimeFormatter.*

@Service
@Transactional(readOnly = true)
class SiteService (
    private val siteRepository: SiteRepository,
    private val memberService: MemberService,
        private val memberRepository: MemberRepository
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

    @Transactional
    fun addSite(request : SiteAddRequestDto): Site{
        // TODO : oauth2 기능 만들고 삭제하기
        val member = Member("password")
        val saveMember = memberService.saveMember(member)
        val saveSite = siteRepository.save(Site(saveMember, request.siteName, request.siteUrl, request.siteCycle))
        // TODO : Entity 내부적으로 연관관계 처리하기
        saveMember.sites.add(saveSite)
        return saveSite
    }

    @Transactional
    fun removeSite(siteId: Long): Long?{
        siteRepository.findByIdOrNull(siteId)
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
        siteRepository.findByIdOrNull(siteId)
            ?.let {
                it.updateDate = it.updateDate.plusDays(it.updateCycle)
                return it.id
            }
            ?: throw SiteException(NOT_FOUND)
    }

    @Transactional
    fun updateSiteInfo(siteId: Long, request: SiteUpdateRequestDto): Long? {
        siteRepository.findByIdOrNull(siteId)?.let {
            it.updateSite(request)
            return it.id
        }
        ?: throw SiteException(NOT_FOUND)
    }
}
