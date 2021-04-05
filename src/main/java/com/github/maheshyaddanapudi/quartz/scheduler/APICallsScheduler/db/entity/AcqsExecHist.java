package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity;

import lombok.*;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity(name = "ACQS_EXEC_HIST")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcqsExecHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id", updatable = false, nullable = false)
    private Long historyId;

    @OneToOne
    @JoinColumn(name = "mappingId")
    @Fetch(FetchMode.SELECT)
    private AcqsSchedMap acqsSchedMap;

    @Column(name = "qrtz_exec_id", length = 150, nullable = true, unique=true, updatable = false)
    private String qrtzExecId;

    @Column(name = "qrtz_exec_status", length = 45)
    private String quartzExecutionStatus;

    @Lob
    @Column(name="qrtz_exec_resp_payload_payload", nullable = true, length=100000)
    private byte[] quartzExecutionApiResponsePayload;

    @Column(name = "qrtz_exec_log", length = 500, nullable = true, unique=false)
    private String quartzExecutionLog;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "insert_timestamp", nullable = false)
    private Date insertTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_timestamp", nullable = false)
    private Date updateTimestamp;

    @PrePersist
    protected void onCreate() {
        updateTimestamp = insertTimestamp = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = new Date();
    }
}
