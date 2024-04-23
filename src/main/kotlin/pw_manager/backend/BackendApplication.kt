package pw_manager.backend

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
class BackendApplication

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}
