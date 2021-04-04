package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedMap;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcqsSchedMapRepository extends JpaRepository<AcqsSchedMap, Long> {
    AcqsSchedMap findBySchedName(String schedName);
    AcqsSchedMap findByMappingId(long mappingId);
    AcqsSchedMap findByQrtzSchedId(String qrtzSchedId);
}
