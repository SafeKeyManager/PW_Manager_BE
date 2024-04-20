package pw_manager.backend.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.persistence.EnumType.*
import jakarta.persistence.FetchType.*
import pw_manager.backend.dto.request.SiteUpdateRequestDto
import pw_manager.backend.entity.Site.SiteStatus.*
import java.time.LocalDateTime
import java.time.LocalDateTime.*

@Entity
class Site(
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_Id")
    @JsonIgnore
    val member: Member,
    var siteName : String,
    var siteUrl : String,
    var updateCycle : Long,
) {
    @Id @GeneratedValue
    val id : Long? = null
    val createDate : LocalDateTime = now()
    var updateDate : LocalDateTime = now().plusDays(30)
    @Enumerated(STRING)
    var siteStatus : SiteStatus = REGIS

    fun updateSite(request: SiteUpdateRequestDto){
        if(request.siteUrl != null){
            siteUrl = request.siteUrl
        }
        if(request.siteName != null){
            siteName = request.siteName
        }
        if(request.updateCycle != null){
            updateCycle = request.updateCycle
        }
    }

    enum class SiteStatus(
        val status: String
    ){
        REGIS("등록"),
        DELETE("삭제")
    }
}
