package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.request.schedule.component;

import lombok.*;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleOAuth2Component {

    @NonNull
    private String oauth2TokenUrl;
    @Nullable
    private String oauth2UserInfoUrl;
    @NonNull
    private String clientId;
    @NonNull
    private String clientSecretBase64;
    @Nullable
    private String grantType;
    @NonNull
    private String username;
    @NonNull
    private String passwordBase64;
}
