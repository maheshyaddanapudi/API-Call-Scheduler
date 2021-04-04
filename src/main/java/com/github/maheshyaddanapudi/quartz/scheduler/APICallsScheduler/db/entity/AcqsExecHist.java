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
    @Column(name = "HISTORY_ID", updatable = false, nullable = false)
    private Long historyId;

    @OneToOne
    @JoinColumn(name = "mappingId")
    @Fetch(FetchMode.SELECT)
    private AcqsSchedMap acqsSchedMap;

    @Column(name = "QRTZ_EXEC_ID", length = 150, nullable = true, unique=true, updatable = false)
    private String qrtzExecId;

    @Column(name = "QRTZ_EXEC_STATUS", length = 45)
    private String quartzExecutionStatus;

    @Lob
    @Column(name="QRTZ_EXEC_RESP_PAYLOAD_PAYLOAD", nullable = true, length=100000)
    private byte[] quartzExecutionApiResponsePayload;

    @Column(name = "QRTZ_EXEC_LOG", length = 500, nullable = true, unique=false)
    private String quartzExecutionLog;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INSERT_TIMESTAMP", nullable = false)
    private Date insertTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIMESTAMP", nullable = false)
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
