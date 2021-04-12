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
    @Column(name = "mapping_id", updatable = false, nullable = false)
    private Long mappingId;

    @Column(name = "qrtz_sched_id", length = 150, nullable = true, unique=true, updatable = false)
    private String qrtzSchedId;

    @Column(name = "sched_name", length = 100, nullable = false, unique=true)
    private String schedName;

    @Column(name = "sched_desc", length = 500, nullable = true, unique=false)
    private String schedDesc;

    @Column(name="sched_api_endpoint", nullable = false, length=500)
    private String schedApiEndpoint;

    @Column(name="sched_api_method", nullable = false, length=10)
    private String schedApiMethod;

    @Lob
    @Column(name="sched_api_payload", nullable = true, length=100000)
    private byte[] schedApiPayload;

    @Lob
    @Column(name="sched_api_headers", nullable = true, length=100000)
    private byte[] schedApiHeaders;

    @Column(name = "sched_cron_expression", length = 45, nullable = false, unique=false)
    private String schedCronExpression;

    @Column(name = "sched_current_status", length = 45, nullable = false, unique=false)
    private String schedCurrentStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sched_start_timestamp", nullable = true)
    private Date schedStartTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sched_stop_timestamp", nullable = false)
    private Date schedStopTimestamp;

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
