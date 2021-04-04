package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExecutionDetails {
    private String qrtzExecId;
    private String quartzExecutionStatus;
    private String quartzExecutionApiResquestPayload;
    private String quartzExecutionApiResponsePayload;
    private String quartzExecutionLog;
    private String startAt;
    private String lastUpdatedAt;

    public ExecutionDetails(String qrtzExecId, String quartzExecutionStatus, String quartzExecutionLog, String startAt, String lastUpdatedAt) {
        this.qrtzExecId = qrtzExecId;
        this.quartzExecutionStatus = quartzExecutionStatus;
        this.quartzExecutionLog = quartzExecutionLog;
        this.startAt = startAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
