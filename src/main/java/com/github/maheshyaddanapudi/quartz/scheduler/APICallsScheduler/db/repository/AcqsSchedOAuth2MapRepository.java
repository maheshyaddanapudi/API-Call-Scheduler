package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedMap;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedOAuth2Map;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcqsSchedOAuth2MapRepository extends JpaRepository<AcqsSchedOAuth2Map, Long> {
    AcqsSchedOAuth2Map findByAcqsSchedMap(AcqsSchedMap acqsSchedMap);
}
