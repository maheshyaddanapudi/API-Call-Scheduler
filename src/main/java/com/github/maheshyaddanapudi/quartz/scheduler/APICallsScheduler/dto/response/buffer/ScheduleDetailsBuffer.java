package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.buffer;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.ScheduleOAuth2DetailsComponent;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleDetailsBuffer {

    private String qrtzSchedId;
    private String schedName;
    private String schedDesc;
    private String schedApiEndpoint;
    private String schedApiMethod;
    private byte[] schedApiPayload;
    private String schedCronExpression;
    private String schedCurrentStatus;
    private Date schedStartTimestamp;
    private Date schedStopTimestamp;
    private ScheduleOAuth2DetailsComponent apiOAuth2Metadata;

}
