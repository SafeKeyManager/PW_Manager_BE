package pw_manager.backend.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pw_manager.backend.dto.request.FcmMessageDto
import pw_manager.backend.dto.request.FcmSendDto
import java.io.IOException
import java.util.*

@Service
class FcmServiceImpl:FcmService{
    override fun sendMessageTo(fcmSendDto: FcmSendDto): Int {
        val message = makeMessage(fcmSendDto)
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers["Authorization"] = "Bearer ${getAccessToken()}"
        val entity = HttpEntity(message, headers)

        val API_URL = "<https://fcm.googleapis.com/v1/projects/pw-manager-97400/messages:send>"
        val response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String::class.java)

        println(response.statusCode)
        return if(response.statusCode == HttpStatus.OK) 1 else 0
    }

    @Throws(IOException::class)
    private fun getAccessToken(): String{
        val firebaseConfigPath = "firebase/firebase_service_key.json"

        val googleCredentials = GoogleCredentials
            .fromStream(ClassPathResource(firebaseConfigPath).inputStream)
            .createScoped("<https://www.googleapis.com/auth/cloud-platform>")
        googleCredentials.refreshIfExpired()
        return googleCredentials.accessToken.tokenValue
    }

    /**
     * FCM 전송 정보를 기반으로 메시지를 구성(Object -> String)
     */
    @Throws(JsonProcessingException::class)
    private fun makeMessage(fcmSendDto: FcmSendDto): Any {
        val om = ObjectMapper()
        val fcmMessageDto = FcmMessageDto(
            false, FcmMessageDto.Message(
                FcmMessageDto.Notification(
                    fcmSendDto.title, fcmSendDto.body, ""
                ), fcmSendDto.token
            )
        )
        return om.writeValueAsString(fcmMessageDto)
    }
}
