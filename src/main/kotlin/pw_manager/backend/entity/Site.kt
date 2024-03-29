package pw_manager.backend.entity

import jakarta.persistence.*
import jakarta.persistence.EnumType.*
import pw_manager.backend.dto.SiteUpdateRequestDto
import pw_manager.backend.entity.Site.SiteStatus.*
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.*

@Entity
class Site(
    var siteName : String,
    var siteUrl : String,
    var updateCycle : Long,   // TODO : responseDto 에서는 String 으로 응답
) {
    @Id @GeneratedValue
    val id : Long? = null
    val createDate : LocalDateTime = now()
    var updateDate : LocalDateTime = now()
    @Enumerated(STRING)
    var siteStatus : SiteStatus = REGIS

    fun updateSite(request:SiteUpdateRequestDto){
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
