package pw_manager.backend.dto.request

data class FcmMessageDto(
    val validateOnly: Boolean = false,
    val message: Message
) {
    data class Message(
        val notification: Notification,
        val token: String
    )

    data class Notification(
        val title: String,
        val body: String,
        val image: String
    )
}
