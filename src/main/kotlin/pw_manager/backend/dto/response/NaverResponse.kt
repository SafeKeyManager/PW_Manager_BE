package pw_manager.backend.dto.response

class NaverResponse(
        private val attributes : MutableMap<String, Any>
) : OAuth2Response {

    private val attribute : MutableMap<String, Any> = attributes["response"] as MutableMap<String, Any>? ?: throw IllegalArgumentException("Response attribute not match format")

    override fun getProvider(): String {
        return "naver"
    }

    override fun getProviderId(): String {
        return attribute["id"].toString()
    }

    override fun getEmail(): String {
        return attribute["email"].toString()
    }

    override fun getName(): String {
        return attribute["name"].toString()
    }
}