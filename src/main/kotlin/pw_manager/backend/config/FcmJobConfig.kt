package pw_manager.backend.config

import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import pw_manager.backend.dto.request.FcmSendDto
import pw_manager.backend.entity.Site
import pw_manager.backend.service.FcmServiceImpl
import java.time.Duration
import java.time.LocalDateTime.*

@Configuration
class FcmJobConfig {

    @Bean
    fun reader(
        entityManagerFactory: EntityManagerFactory
    ): JpaPagingItemReader<Site>{
        val param = hashMapOf<String, Any>()
        param["day"] = now()
        return JpaPagingItemReaderBuilder<Site>()
            .pageSize(10)
            .parameterValues(param)
            .queryString("select s from Site s join fetch s.member m where s.updateDate <= :day and s.siteStatus != 'DELETE'")
            .entityManagerFactory(entityManagerFactory)
            .name("JpaPagingItemReader")
            .build()

    }

    @Bean
    fun writer(
        fcmServiceImpl: FcmServiceImpl
    ) : ItemWriter<Site>{
        return ItemWriter { chunk ->
            chunk.forEach {
                site ->
                    val deviceToken = site.member.deviceToken
                    val duration = Duration.between(site.updateDate, now()).toDays()
                    fcmServiceImpl.sendMessageTo(FcmSendDto(deviceToken, "${site.siteName} 비밀번호를 변경해주세요", "변경한지 ${duration}일 지났습니다"))
                    println("fcm batch 실행")
            }
        }
    }

    @Bean
    fun notificationStep(
            jobRepository: JobRepository,
            reader: JpaPagingItemReader<Site>,
            writer: ItemWriter<Site>,
            transactionManager: PlatformTransactionManager
    ): Step{
        return StepBuilder("fcm-send-step", jobRepository)
            .chunk<Site, Site>(10, transactionManager)
            .reader(reader)

            .writer(writer)
            .build();
    }

    @Bean
    fun notificationJob(notification: Step, jobRepository: JobRepository): Job {
        return JobBuilder("fcm-send-job", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(notification)
            .build()
    }
}
