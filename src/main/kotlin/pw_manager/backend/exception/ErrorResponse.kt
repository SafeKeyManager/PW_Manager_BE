package pw_manager.backend.exception

import org.springframework.http.ResponseEntity
import java.time.LocalDateTime
import java.time.LocalDateTime.now

class ErrorResponse(
    val timestamp: LocalDateTime,
    val status:Int,
    val error:String,
    val code:String,
    val message: Map<String, String> = emptyMap()      // TODO : error 발생 이유 전달, 굳이 필요할까 싶기도 하고
) {
//    constructor(status: Int, error: String) : this(now(), status, emptyMap())
//    constructor(status: Int, error: String, message: Map<String, String>?) : this(now(), status, message)
    companion object{
        fun errorResponse(e: ErrorCode) : ResponseEntity<ErrorResponse>{
            return ResponseEntity
                .status(e.status)
                .body(
                    ErrorResponse(
                        now(), e.status, e.error ,e.code
                    )
                )
        }
    }
}
