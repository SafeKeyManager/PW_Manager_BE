package pw_manager.backend.dto.response

data class SiteAddResponseDto(
    val siteId: Long,
    val siteName: String,
    val siteUrl: String,
    val updateCycle: String,
    val createDate: String,
    val updateDate: String
) {
}
