package pw_manager.backend.dto.request

data class FcmSendDto (
    val token : String,
    val title : String,
    val body : String
)
