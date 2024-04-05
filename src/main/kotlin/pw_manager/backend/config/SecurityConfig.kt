package pw_manager.backend.config

import jakarta.servlet.http.Cookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pw_manager.backend.service.CustomOAuth2UserService
import pw_manager.backend.util.JWTFilter
import pw_manager.backend.util.JWTUtil

@Configuration
class SecurityConfig(
        private val customOAuth2UserService: CustomOAuth2UserService,
        private val jwtUtil: JWTUtil
) {

    @Bean
    open fun filterchain(http: HttpSecurity) : SecurityFilterChain {

        http.csrf{ csrf -> csrf.disable() }
        http.formLogin { formlogin -> formlogin.disable() }
        http.httpBasic { httpbasic -> httpbasic.disable() }

        http.addFilterBefore(JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java )

        http.oauth2Login{
            oauth2 -> oauth2.userInfoEndpoint{
                endpoint -> endpoint.userService(customOAuth2UserService)
            }
                .successHandler { request, response, authentication ->
                    val jwtToken : String = jwtUtil.createJwt(authentication.getName(), "USER", 60 * 60 * 10L)
                    println(jwtToken)
                    val redirectUri = "secretmanagerapp://oauthcallback?token=${jwtToken}"
                    response.sendRedirect(redirectUri)
                }
        }

        http.authorizeHttpRequests {
            authz -> authz
                    .requestMatchers("/","/oauth2/**").permitAll()
                    .anyRequest().authenticated()
        }

        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        return http.build()
    }

    /*fun createCookie(key: String, value: String) : Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 60*60*60
        cookie.path = "/"
        cookie.isHttpOnly = true
        //cookie.secure = true

        return cookie
    }*/
}