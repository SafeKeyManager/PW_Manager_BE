package pw_manager.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    open fun filterchain(http: HttpSecurity) : SecurityFilterChain {

        http.csrf{
            csrf -> csrf.disable()
        }

        return http.build()
    }

}