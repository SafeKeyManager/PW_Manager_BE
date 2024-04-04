package pw_manager.backend.dto.response

interface OAuth2Response {

    fun getProvider(): String
    fun getProviderId(): String
    fun getEmail(): String
    fun getName(): String
}