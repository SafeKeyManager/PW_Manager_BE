package pw_manager.backend.util

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import pw_manager.backend.entity.Member
import pw_manager.backend.user.CustomOAuth2User

class JWTFilter(
        private val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        val authorization = request.getHeader("Authorization")

        if(authorization == null || !authorization.startsWith("Bearer ")){
            println("token null")
            filterChain.doFilter(request, response)
            return
        }

        println("authorization now")
        val token = authorization.split(" ")[1]

        if(jwtUtil.isExpired(token)){
            println("token expired")
            filterChain.doFilter(request, response)
            return
        }

        val username = jwtUtil.getUsername(token)
        //val role = jwtUtil.getRole(token)

        val customOAuth2User = CustomOAuth2User(Member(username))

        val authenticationToken = UsernamePasswordAuthenticationToken(customOAuth2User, null)

        SecurityContextHolder.getContext().authentication = authenticationToken

        filterChain.doFilter(request, response)
    }

}