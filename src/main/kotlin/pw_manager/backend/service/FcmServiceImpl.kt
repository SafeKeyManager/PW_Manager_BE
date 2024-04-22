package pw_manager.backend.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import lombok.extern.slf4j.Slf4j
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pw_manager.backend.dto.request.FcmMessageDto
import pw_manager.backend.dto.request.FcmSendDto
import java.io.IOException
import java.time.Duration
import java.time.LocalDateTime.*
import java.util.*

@Service
@Slf4j
class FcmServiceImpl(
    private val siteService: SiteService
):FcmService{

    /**
     * schedule을 통해 db에서 기간이 경과된 site에 대한 fcm push(하루 주기로 실행)
     */
    // TODO : spring batch 사용해서 성능 최적화?
    @Scheduled(cron = "* * 1 * * *")
    fun checkSitePeriod(){
        // TODO : logger 로 출력바꾸기
        println("Scheduler 실행")
        val expiredSites = siteService.getAllExpiredSiteAndUser()
        for (site in expiredSites) {
            val deviceToken = site.member.deviceToken
            val duration = Duration.between(site.updateDate, now()).toDays()
            sendMessageTo(FcmSendDto(deviceToken, "${site.siteName} 비밀번호를 변경해주세요", "변경한지 ${duration}일 지났습니다"))
        }

    }
    override fun sendMessageTo(fcmSendDto: FcmSendDto): Int {
        println("fcmSendDto = ${fcmSendDto}")
        val message = makeMessage(fcmSendDto)
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers["Authorization"] = "Bearer ${getAccessToken()}"
        val entity = HttpEntity(message, headers)

        val API_URL = ("https://fcm.googleapis.com/v1/projects/pw-manager-97400/messages:send")
        val response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String::class.java)

        println(response.statusCode)
        return if(response.statusCode == HttpStatus.OK) 1 else 0
    }

    @Throws(IOException::class)
    private fun getAccessToken(): String {
        val firebaseConfigPath = "firebase/firebase_service_key.json"

        val googleCredentials = GoogleCredentials
            .fromStream(ClassPathResource(firebaseConfigPath).inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))
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
