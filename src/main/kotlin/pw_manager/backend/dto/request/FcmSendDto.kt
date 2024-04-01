package pw_manager.backend.dto.request

data class FcmSendDto (
    val token : String,
    val title : String,
    val body : String

) {
    override fun toString(): String {
        return "FcmSendDto(token='$token', title='$title', body='$body')"
    }
}
