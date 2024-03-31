package pw_manager.backend.service

import pw_manager.backend.dto.request.FcmSendDto

interface FcmService {
    fun sendMessageTo(fcmSendDto: FcmSendDto) : Int
}
