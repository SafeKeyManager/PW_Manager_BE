package pw_manager.backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pw_manager.backend.entity.Member
import pw_manager.backend.service.MemberService

@RestController
class MainController (
    private val memberService: MemberService
){
    @GetMapping("/")
    fun mainPage(): String {
        return "hello world"
    }

    @GetMapping("/members")
    fun findAllMember(): List<Member> {
        return memberService.findAllMember()
    }
}
