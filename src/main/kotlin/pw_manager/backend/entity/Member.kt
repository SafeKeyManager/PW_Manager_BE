package pw_manager.backend.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
class Member (
    @Id @GeneratedValue
    val id : Long,
    val userHash: String,
    val email : String,
    val createDate: LocalDate,
    val editDate: LocalDate
){

}

