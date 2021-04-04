package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.downstream.token;

import lombok.*;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessTokenDTO {

    @NotNull
    private String access_token;

    private int responseStatusCode;
    private String responseErrorMsg;
}
