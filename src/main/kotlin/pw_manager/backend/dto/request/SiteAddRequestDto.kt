package pw_manager.backend.dto.request

import jakarta.validation.constraints.NotNull


// TODO : validation을 위해서 nullable로 선언하는게 맞는가
//  validation check 가 안됨
data class SiteAddRequestDto(
    @field: NotNull(message = "사이트 이름은 필수입니다")
    val siteName : String,
    @field: NotNull(message = "사이트 URL은 필수입니다")
    val siteUrl : String,
    @field: NotNull(message = "변경주기는 필수입니다")
    val siteCycle : Long
)
