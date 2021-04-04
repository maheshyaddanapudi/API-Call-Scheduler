package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.request.schedule.component.ScheduleOAuth2Component;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleDetails {

    private String scheduleId;
    private String apiEndPoint;
    private String apiMethod;
    private String apiPayload;
    private String scheduleName;
    private String scheduleDesc;
    private String cronExpression;
    private String scheduleStatus;
    private String startDate;
    private String endDate;
    private ScheduleOAuth2DetailsComponent apiOAuth2Metadata;

    public ScheduleDetails(String apiEndPoint, String apiMethod, String scheduleName, String scheduleDesc, String cronExpression, String scheduleStatus, String startDate, String endDate) {
        this.apiEndPoint = apiEndPoint;
        this.apiMethod = apiMethod;
        this.scheduleName = scheduleName;
        this.scheduleDesc = scheduleDesc;
        this.cronExpression = cronExpression;
        this.scheduleStatus = scheduleStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
