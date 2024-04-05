package pw_manager.backend.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import lombok.Setter
import java.time.LocalDate
import java.time.LocalDate.*

@Entity
class Member (
    val userHash: String,
){
    @Id @GeneratedValue
    val id : Long? = null
    val createDate: LocalDate = now()
    var deviceToken: String = ""
    @OneToMany(mappedBy = "member")
    val sites: MutableList<Site> = arrayListOf()


}

