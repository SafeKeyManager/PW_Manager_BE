package pw_manager.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    open fun filterchain(http: HttpSecurity) : SecurityFilterChain {

        http.csrf{ csrf -> csrf.disable() }
        http.formLogin { formlogin -> formlogin.disable() }
        http.httpBasic { httpbasic -> httpbasic.disable() }
        http.oauth2Login(Customizer.withDefaults())

        http.authorizeHttpRequests {
            authz -> authz.requestMatchers("/","/oauth2/**","/getTestUser")
        }

        return http.build()
    }

}