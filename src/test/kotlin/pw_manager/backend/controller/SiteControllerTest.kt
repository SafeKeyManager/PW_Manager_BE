package pw_manager.backend.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pw_manager.backend.RestDocsConfiguration
import pw_manager.backend.dto.request.SiteAddRequestDto
import pw_manager.backend.dto.request.SiteUpdateRequestDto
import pw_manager.backend.dto.response.SiteAddResponseDto
import pw_manager.backend.entity.Member
import pw_manager.backend.entity.Site
import pw_manager.backend.service.SiteService
import pw_manager.backend.util.JWTUtil


@ExtendWith(SpringExtension::class)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration::class)
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

    @Test
    fun `정상적으로 사이트 가져오기 getSiteList_200`(){
        val totalSite : Long = 50
        val siteList = mutableListOf<SiteAddResponseDto>()
        for(i in 1..totalSite){
            siteList.add(generateSiteAddResponseDto(i))
        }
        val pageable: Pageable = PageRequest.of(2, 10)
        val pageImpl = PageImpl(siteList.subList(20,30), pageable,totalSite)

        every { siteService.getAllList(any(), pageable) } returns pageImpl

        mockMvc.perform(get("/site")
                .header("Authorization","Bearer ${jwtToken}")
                .param("searchDto","")
                .param("page","2")
                .param("size","10"))
                .andDo(print())
                .andExpect(status().isOk)
                .andDo(document("getSiteList"))
    }

    @Test
    fun `정상적으로 유저의 사이트 가져오기 getMySiteList_200`(){
        val totalSite : Int = 50
        val siteList = mutableListOf<Site>()
        for(i in 1..totalSite){
            siteList.add(generateSite(i))
        }
        val pageable: Pageable = PageRequest.of(2, 10)
        val pageImpl = PageImpl(siteList.subList(20,30), pageable,totalSite+0L)

        every { siteService.getAllMyList(any(), pageable) } returns pageImpl

        mockMvc.perform(get("/sites")
                .header("Authorization","Bearer ${jwtToken}")
                .param("searchDto","")
                .param("page","2")
                .param("size","10"))
                .andDo(print())
                .andExpect(status().isOk)
                .andDo(document("getMySiteList"))
    }


    @Test
    fun `정상적으로 사이트 추가 addSite_201`(){
        every { siteService.addSite(any()) } returns Site(Member("member1"), "국민대학교","https://kookmin.co.kr",12 )
        val json = jacksonObjectMapper().writeValueAsString(generateSiteAddRequestDto(0))

        mockMvc.perform(post("/site/add")
                .header("Authorization","Bearer ${jwtToken}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated)
                .andDo(document("addSite", responseFields(
                        fieldWithPath("id").description("사이트 DB Id").type(Long::class),
                        fieldWithPath("siteName").description("사이트 이름"),
                        fieldWithPath("siteUrl").description("사이트 주소"),
                        fieldWithPath("updateCycle").description("비밀번호 변경 주기"),
                        fieldWithPath("createDate").description("사이트 추가 날짜"),
                        fieldWithPath("updateDate").description("다음 비밀번호 변경 날짜"),
                        fieldWithPath("siteStatus").description("사이트 현재 상태"),
                )))
    }

    @Test
    fun `정상적으로 사이트 삭제 removeSite_200`(){
        val siteId : Long = 3
        every { siteService.removeSite(siteId) } returns siteId

        mockMvc.perform(delete("/site/{siteId}/delete",siteId)
                .header("Authorization","Bearer ${jwtToken}"))
                .andDo(print())
                .andExpect(status().isOk)
                .andDo(document("removeSite"))
    }

    @Test
    fun `정상적으로 비밀번호 변경 updateCycle_200`(){
        val siteId : Long = 3
        every { siteService.updateCycle(siteId) } returns siteId

        mockMvc.perform(put("/site/{siteId}/updateCycle",siteId)
                .header("Authorization","Bearer ${jwtToken}"))
                .andDo(print())
                .andExpect(status().isOk)
                .andDo(document("updateCycle"))
    }

    @Test
    fun `정상적으로 사이트 정보 변경 updateSiteInfo_200`(){
        val siteId : Long = 3
        val request : SiteUpdateRequestDto = generateSiteUpdateRequestDto()
        every { siteService.updateSiteInfo(siteId, request) } returns siteId

        val json = jacksonObjectMapper().writeValueAsString(request)

        mockMvc.perform(post("/site/{siteId}/update",siteId)
                .header("Authorization","Bearer ${jwtToken}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk)
                .andDo(document("updateSiteInfo"))
    }



    fun generateSiteAddRequestDto(index : Int) : SiteAddRequestDto = SiteAddRequestDto("국민대학교 ${index}","https://kookmin.co.kr",12)
    fun generateSiteAddResponseDto(index : Long) : SiteAddResponseDto = SiteAddResponseDto(index, "${index}번째 사이트","http://www.${index}site.com","12","20240404","20250404")
    fun generateSite(index: Int) : Site = Site(Member("member1"),"국민대학교 ${index}","https://kookmin.co.kr",12)
    fun generateSiteUpdateRequestDto() : SiteUpdateRequestDto = SiteUpdateRequestDto("민국대학교","http://minkook.co.kr",24L)
}