package pw_manager.backend.controller

import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pw_manager.backend.dto.SearchDto
import pw_manager.backend.dto.SiteAddRequestDto
import pw_manager.backend.dto.SiteUpdateRequestDto
import pw_manager.backend.entity.Site
import pw_manager.backend.service.SiteService

@RestController
@Slf4j
class SiteController (
    private val siteService: SiteService
){

    @GetMapping("/site")
    fun getSiteList (
        searchDto: SearchDto,
        @PageableDefault(size = 10)
        pageable: Pageable
    ):ResponseEntity<Page<Site>>{
        return ResponseEntity.ok(siteService.getAllList(searchDto, pageable))
    }

    @PostMapping("/site/add")
    fun addSite(
        @RequestBody @Validated
        request: SiteAddRequestDto
    ) {
        return siteService.addSite(request)
    }

    // TODO : request 요청을 post가 맞는지 delete가 맞는지 아직 고민중
    @PostMapping("/site/delete/{siteId}")
    fun removeSite(@PathVariable("siteId") siteId: Long){
        return siteService.removeSite(siteId)
    }

    // 갱신주기가 되서 유저가 갱신
    // TODO : post 말고 put으로 하는것도 고려해보기
    @PostMapping("/site/updateCycle/{siteId}")
    fun updateCycle(
        @PathVariable("siteId") siteId: Long
    ) {
        return siteService.updateCycle(siteId)
    }

    @PostMapping("/site/update/{siteId}")
    fun updateSiteInfo(
        @PathVariable siteId: Long,
        @RequestBody request : SiteUpdateRequestDto
    ) : String{
        return siteService.updateSiteInfo(siteId, request)
    }



}
