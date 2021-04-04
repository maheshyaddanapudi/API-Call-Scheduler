package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsExecHist;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcqsExecHistRepository extends JpaRepository<AcqsExecHist, Long> {

    List<AcqsExecHist> findByAcqsSchedMap(AcqsSchedMap acqsSchedMap);

    AcqsExecHist findByQrtzExecId(String qrtzExecId);

    @Modifying
    @Query(value = "DELETE FROM ACQS_EXEC_HIST WHERE UPDATE_TIMESTAMP < ?1", nativeQuery = true)
    void deleteRecordsOlderThan(String dateBefore);
}
