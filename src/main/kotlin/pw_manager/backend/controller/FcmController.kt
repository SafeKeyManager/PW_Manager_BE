package pw_manager.backend.controller

import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pw_manager.backend.dto.request.FcmSendDto
import pw_manager.backend.service.FcmService

class ApiResponseWrapper<T>(
    val result: T,
    val resultCode: Int,
    val resultMsg: String
)

@Slf4j
@RestController
@RequestMapping("/api/v1/fcm")
class FcmController(
    private val fcmService: FcmService,
){
    @PostMapping("/send")
    fun pushMessage(
        @RequestBody @Validated fcmSendDto: FcmSendDto
    ): String{
        println("[+] 푸시 메시지를 전송합니다. ")
        val result = fcmService.sendMessageTo(fcmSendDto)
        return "ok"
    }

    @PostMapping("/token")
    fun getDeviceToken(
        @RequestBody fcmtoken:String
    ) : ResponseEntity<String>{
        println("받은 fcmdevicetoken :  $fcmtoken")
        return ResponseEntity.ok().body(fcmtoken)
    }
}
