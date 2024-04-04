package pw_manager.backend.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig
import org.springframework.security.web.SecurityFilterChain
import pw_manager.backend.service.CustomOAuth2UserService

@Configuration
class SecurityConfig(
        private val customOAuth2UserService: CustomOAuth2UserService
) {



    @Bean
    open fun filterchain(http: HttpSecurity) : SecurityFilterChain {

        http.csrf{ csrf -> csrf.disable() }
        http.formLogin { formlogin -> formlogin.disable() }
        http.httpBasic { httpbasic -> httpbasic.disable() }
        http.oauth2Login{
            oauth2 -> oauth2.userInfoEndpoint{
                endpoint -> endpoint.userService(customOAuth2UserService)
            }
        }

        http.authorizeHttpRequests {
            authz -> authz
                    .requestMatchers("/","/oauth2/**").permitAll()
                    .anyRequest().authenticated()
        }

        return http.build()
    }

}