package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.mappers.ScheduleMapper;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.APICallQuartzExecutionHistoryService;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.internal.cipher.EncryptionDecryptionService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		SecurityAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class,
		FlywayAutoConfiguration.class
		})
@EnableBatchProcessing
@EnableTransactionManagement
@EnableScheduling
@Slf4j
public class ApiCallsSchedulerApplication {

	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;

	public static void main(String[] args) {
		SpringApplication.run(ApiCallsSchedulerApplication.class, args);
	}

	@Bean
	public Gson gson(){
		return new Gson();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startScheduler() throws SchedulerException {
		log.info("Verifying Scheduler Startup status ...");
		if (schedulerFactoryBean.getScheduler().isShutdown()) {
			schedulerFactoryBean.getScheduler().start();
			log.warn("Scheduler Start Triggered !!!");
		} else
			log.info("Scheduler already in started status ...");
	}

}
