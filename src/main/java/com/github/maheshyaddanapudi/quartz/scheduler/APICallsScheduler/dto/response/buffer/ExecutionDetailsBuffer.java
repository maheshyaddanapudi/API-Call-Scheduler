package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.buffer;

import lombok.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExecutionDetailsBuffer {
    private String qrtzExecId;
    private String quartzExecutionStatus;
    private byte[] quartzExecutionApiResponsePayload;
    private String quartzExecutionLog;
    private Date insertTimestamp;
    private Date updateTimestamp;
}
