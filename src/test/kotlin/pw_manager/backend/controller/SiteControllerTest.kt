package pw_manager.backend.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pw_manager.backend.dto.request.SiteAddRequestDto
import pw_manager.backend.entity.Member
import pw_manager.backend.entity.Site
import pw_manager.backend.service.SiteService
import pw_manager.backend.util.JWTUtil

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SiteControllerTest{
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    @MockkBean
    private lateinit var siteService: SiteService

    private lateinit var jwtToken : String

    @BeforeEach
    fun generateJwtToken(){
        jwtToken = jwtUtil.createJwt("member1", "USER", 60 * 60 * 60 * 10L)
    }

    @BeforeEach
    fun siteServiceSetup(){
        every { siteService.addSite(generateSiteAddRequestDto()) } returns Site(Member("member1"), "국민대학교","https://kookmin.co.kr",12 )
    }

    @Test
    //@WithMockUser(username = "member1")
    fun `정상적으로 site를 추가`(){

        val json = jacksonObjectMapper().writeValueAsString(generateSiteAddRequestDto())

        mockMvc.perform(post("/site/add")
                .header("Authorization","Bearer ${jwtToken}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated)

    }

    fun generateSiteAddRequestDto() : SiteAddRequestDto = SiteAddRequestDto("국민대학교","https://kookmin.co.kr",12)


}