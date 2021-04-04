package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleOAuth2DetailsComponent {
    private String oauth2TokenUrl;
    private String oauth2UserInfoUrl;
    private String oauth2GrantType;
    private String oauth2ClientId;
    private String oauth2Username;

}
