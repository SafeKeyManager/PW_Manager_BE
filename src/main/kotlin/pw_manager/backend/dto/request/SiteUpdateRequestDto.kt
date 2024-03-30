package pw_manager.backend.dto.request

data class SiteUpdateRequestDto(
    val siteName:String? = null,
    val siteUrl:String? = null,
    val updateCycle:Long? = null
)
