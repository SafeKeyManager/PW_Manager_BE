package pw_manager.backend.controller

import lombok.extern.slf4j.Slf4j
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pw_manager.backend.dto.request.FcmSendDto
import pw_manager.backend.service.FcmService
import pw_manager.backend.service.MemberService

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
    private val memberService: MemberService
){
    @PostMapping("/send")
    fun pushMessage(
        @RequestBody @Validated fcmSendDto: FcmSendDto
    ): String{
        println("[+] 푸시 메시지를 전송합니다. ")
        val result = fcmService.sendMessageTo(fcmSendDto)
        return "ok"
    }

    // TODO : 로그인 따로 fcm token 따로하기 보다는 로그인할떄 additionalParameter로 한번에 받아오기?
    @PostMapping("/token")
    fun getDeviceToken(
        @RequestBody fcmtoken:String,
    ) : String{
        println("받은 fcmdevicetoken :  $fcmtoken")
        memberService.saveFcmTokenToMember(fcmtoken)
        return "ok"
    }
}
