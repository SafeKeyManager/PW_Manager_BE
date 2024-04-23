package pw_manager.backend.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

/**
 * spring batch 트리거
 */
@Configuration
class JobSchduler (
    private val jobLauncher: JobLauncher,
    private val notificationJob: Job
){
    @Scheduled(cron = "* * 1 * * *")    //하루마다 실행
    fun sendFcmMessage(){
        println("service 시작")
        val jobParameters = JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters()
        jobLauncher.run(notificationJob, jobParameters)
    }
}
