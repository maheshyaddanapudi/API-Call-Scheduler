package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ACQS_SCHED_MAP")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcqsSchedMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAPPING_ID", updatable = false, nullable = false)
    private Long mappingId;

    @Column(name = "QRTZ_SCHED_ID", length = 150, nullable = true, unique=true, updatable = false)
    private String qrtzSchedId;

    @Column(name = "SCHED_NAME", length = 100, nullable = false, unique=true)
    private String schedName;

    @Column(name = "SCHED_DESC", length = 500, nullable = true, unique=false)
    private String schedDesc;

    @Column(name="SCHED_API_ENDPOINT", nullable = false, length=500)
    private String schedApiEndpoint;

    @Column(name="SCHED_API_METHOD", nullable = false, length=10)
    private String schedApiMethod;

    @Lob
    @Column(name="SCHED_API_PAYLOAD", nullable = true, length=100000)
    private byte[] schedApiPayload;

    @Column(name = "SCHED_CRON_EXPRESSION", length = 45, nullable = false, unique=false)
    private String schedCronExpression;

    @Column(name = "SCHED_CURRENT_STATUS", length = 45, nullable = false, unique=false)
    private String schedCurrentStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SCHED_START_TIMESTAMP", nullable = true)
    private Date schedStartTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SCHED_STOP_TIMESTAMP", nullable = false)
    private Date schedStopTimestamp;

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
