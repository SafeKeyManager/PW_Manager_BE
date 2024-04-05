package pw_manager.backend.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pw_manager.backend.dto.request.SiteAddRequestDto;
import pw_manager.backend.util.JWTUtil;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring_jwt_secret=qwertyuiopasdfghjklzxcvbnmqwerty"})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerTest{
    @Autowired
    MockMvc mockMvc;


    @Test
    @DisplayName("정상적인 site 생성")
    @WithCustomMockUser
    public void createSite() throws Exception {

        var siteAddRequestDto = new SiteAddRequestDto("국민대학교","https://cs.kookmin.ac.kr/",12);
        String json = jacksonObjectMapper().writeValueAsString(siteAddRequestDto);
        System.out.println(json);
        mockMvc.perform(post("/site/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.siteName").value("국민대학교"))
                .andExpect(jsonPath("$.siteUrl").value("https://cs.kookmin.ac.kr/"))
                .andExpect(jsonPath("$.updateCycle").value(12))
        ;
    }

    @Test
    @DisplayName("fcm토큰 정상 전송")
    @WithCustomMockUser
    public void sendFcmToken() throws Exception{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fcmtoken","thisisfcmtoken");

        mockMvc.perform(post("/api/v1/fcm/token")
                .content(jsonObject.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("fcmtoken").exists())
                ;
    }

}
