package pw_manager.backend.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(SiteException::class)
    fun siteException(e: SiteException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.errorResponse(e.errorCode)
    }
}
