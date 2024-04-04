package pw_manager.backend.controller

import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import pw_manager.backend.dto.request.SearchDto
import pw_manager.backend.dto.request.SiteAddRequestDto
import pw_manager.backend.dto.request.SiteUpdateRequestDto
import pw_manager.backend.dto.response.IdResponseDto
import pw_manager.backend.dto.response.SiteAddResponseDto
import pw_manager.backend.entity.Site
import pw_manager.backend.service.SiteService
import pw_manager.backend.user.CustomOAuth2User

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
    ):ResponseEntity<Page<SiteAddResponseDto>>{
        return ResponseEntity.ok(siteService.getAllList(searchDto, pageable))
    }

    @PostMapping("/site/add")
    fun addSite(
            @RequestBody @Validated
            request: SiteAddRequestDto
    ): ResponseEntity<Site>{
        val site = siteService.addSite(request)
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(site.id)
                .toUri()

        return ResponseEntity.created(location).body(site)
    }

    // TODO : request 요청을 post가 맞는지 delete가 맞는지 아직 고민중
    @PostMapping("/site/{siteId}/delete")
    fun removeSite(@PathVariable("siteId") siteId: Long): ResponseEntity<IdResponseDto>{
        return ResponseEntity.ok(IdResponseDto(siteService.removeSite(siteId)))
    }

    // 갱신주기가 되서 유저가 갱신
    // TODO : post 말고 put으로 하는것도 고려해보기
    @PostMapping("/site/{siteId}/updateCycle")
    fun updateCycle(
        @PathVariable("siteId") siteId: Long
    ):ResponseEntity<IdResponseDto>{
        return ResponseEntity.ok(IdResponseDto(siteService.updateCycle(siteId)))
    }

    @PostMapping("/site/{siteId}/update")
    fun updateSiteInfo(
        @PathVariable siteId: Long,
        @RequestBody request : SiteUpdateRequestDto
    ) : ResponseEntity<IdResponseDto>{
        return ResponseEntity.ok(IdResponseDto(siteService.updateSiteInfo(siteId, request)))
    }
}
