package pw_manager.backend.exception

import java.lang.RuntimeException

class SiteException(
    val errorCode: ErrorCode
): RuntimeException()
