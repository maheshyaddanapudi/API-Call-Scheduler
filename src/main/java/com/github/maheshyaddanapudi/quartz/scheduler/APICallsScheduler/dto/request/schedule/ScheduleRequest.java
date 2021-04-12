package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.request.schedule;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.request.schedule.component.ScheduleOAuth2Component;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleRequest {

    @NonNull
    private String apiEndPoint;
    @NonNull
    private String apiMethod;
    @Nullable
    private String apiPayload;
    @Nullable
    private Map<String, Object> apiHeaders;
    @NonNull
    private String scheduleName;
    @Nullable
    private String scheduleDesc;
    @NonNull
    private String cronExpression;
    @Nullable
    private String startDate;
    @NonNull
    private String endDate;
    @Nullable
    private ScheduleOAuth2Component apiOAuth2Metadata;
}
