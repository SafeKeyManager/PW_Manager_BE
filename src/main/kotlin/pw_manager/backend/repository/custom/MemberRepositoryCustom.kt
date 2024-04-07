package pw_manager.backend.repository.custom

import pw_manager.backend.entity.Member

interface MemberRepositoryCustom {
    fun findUserAndSites(userHash: String):Member?

}
