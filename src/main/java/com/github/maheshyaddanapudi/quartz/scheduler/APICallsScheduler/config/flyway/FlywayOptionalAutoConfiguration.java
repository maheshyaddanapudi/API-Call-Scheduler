package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.config.flyway;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

@Configuration
@Profile(value = {Constants.EMBEDDED_OAUTH2})
@Slf4j
public class FlywayOptionalAutoConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean(initMethod = Constants.MIGRATE)
    public Flyway flyway() {
        log.debug("Initializing flyway from FlywayOptionalAutoConfiguration");
        Flyway flyway = new Flyway();
        flyway.setValidateOnMigrate(false);
        flyway.setBaselineVersionAsString(Constants.ZERO);
        flyway.setBaselineOnMigrate(true);
        flyway.setDataSource(dataSource);
        return flyway;
    }
}