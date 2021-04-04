package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.config.auto.cleanup;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.APICallQuartzExecutionHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@ConditionalOnProperty(name = "auto-cleanup.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class AutoCleanupConfiguration {

    static {
        log.info("AutoCleanupConfiguration is enabled !!!");
    }

    @Autowired
    APICallQuartzExecutionHistoryService apiCallQuartzExecutionHistoryService;

    @Value("auto-cleanup.delete-records-older-than-days:30")
    public String AUTO_CLEANUP_THRESHOLD_NO_OF_DAYS;

    @Value("auto-cleanup.enabled:false")
    public String AUTO_CLEANUP_ENABLED;

    @Scheduled(cron = "0 0 0 * * ?")
    public void performExecutionHistoryCleanup(){
        if(AUTO_CLEANUP_ENABLED.equalsIgnoreCase(Constants.TRUE))
        {
            log.info("Performing Auto-Cleanup of execution history records greater than {} days", AUTO_CLEANUP_THRESHOLD_NO_OF_DAYS);
            apiCallQuartzExecutionHistoryService.deleteRecordsOlderThan(Integer.parseInt(AUTO_CLEANUP_THRESHOLD_NO_OF_DAYS));
        }
        else
            log.warn("Auto Clean Up is disabled. Set auto-cleanup.enabled to true to enable it.");
    }
}
