package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.job;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsExecHist;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedMap;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedOAuth2Map;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository.AcqsExecHistRepository;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository.AcqsSchedMapRepository;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository.AcqsSchedOAuth2MapRepository;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.downstream.token.AccessTokenDTO;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.downstream.rest.oauth2.DownstreamOAuthLoginService;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.internal.cipher.EncryptionDecryptionService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

@Component
@Slf4j
public class ApiCallExecutionJob extends QuartzJobBean implements Serializable {

    private static final long serialVersionUID = -875336427502820536L;

    @Autowired
    private DownstreamOAuthLoginService downstreamOAuthLoginService;

    @Autowired
    private AcqsSchedMapRepository acqsSchedMapRepository;

    @Autowired
    private AcqsSchedOAuth2MapRepository acqsSchedOAuth2MapRepository;

    @Autowired
    private AcqsExecHistRepository acqsExecHistRepository;

    @Autowired
    private EncryptionDecryptionService encryptionDecryptionService;

    @Autowired
    private RestTemplate restTemplate;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        long mappingId = jobDataMap.getLong(Constants.SCHEDULE_MAPPING_ID);
        String runLog = Constants.STRING_INITIALIZR;

        if(mappingId>0)
        {
            AcqsSchedMap acqsSchedMap = this.acqsSchedMapRepository.findByMappingId(mappingId);

            if(null!=acqsSchedMap)
            {
                boolean isAuthFailed = false;
                String uniqueIdentifier = UUID.randomUUID().toString();

                AcqsExecHist acqsExecHist = new AcqsExecHist();

                acqsExecHist.setQrtzExecId(uniqueIdentifier);
                acqsExecHist.setAcqsSchedMap(acqsSchedMap);
                acqsExecHist.setQuartzExecutionStatus("INITIALIZED");

                acqsExecHist = this.acqsExecHistRepository.saveAndFlush(acqsExecHist);

                HttpHeaders headers = new HttpHeaders();

                AcqsSchedOAuth2Map acqsSchedOAuth2Map = this.acqsSchedOAuth2MapRepository.findByAcqsSchedMap(acqsSchedMap);
                if(null!=acqsSchedOAuth2Map)
                {
                    runLog = runLog+"## Performing OAuth2 Authenticaion\n\t"+acqsSchedOAuth2Map.getOauth2TokenUrl()+"\n";
                    AccessTokenDTO accessTokenDto = this.downstreamOAuthLoginService.performOAuth2Authentication(
                            acqsSchedOAuth2Map.getOauth2ClientId(),
                            this.encryptionDecryptionService.decrypt(acqsSchedOAuth2Map.getOauth2ClientSecretEncrypted()),
                            acqsSchedOAuth2Map.getOauth2Username(),
                            this.encryptionDecryptionService.decrypt(acqsSchedOAuth2Map.getOauth2PasswordEncrypted()),
                            acqsSchedOAuth2Map.getOauth2TokenUrl()
                            );

                    if(accessTokenDto.getResponseErrorMsg() == null)
                    {
                        runLog = runLog+"## OAuth2 Authenticaion Success\n\t"+accessTokenDto.getResponseStatusCode()+"\n";
                        headers.set("Authorization", "Bearer "+accessTokenDto.getAccess_token());
                    }
                    else{
                        isAuthFailed = true;
                        runLog = runLog+"## OAuth2 Authenticaion Failed\n\t"+accessTokenDto.getResponseErrorMsg()+"\n";
                    }

                }

                try {

                    if(null!=acqsSchedMap.getSchedApiHeaders())
                    {
                        Gson gson = new Gson();

                        Type type = new TypeToken<Map<String, String>>(){}.getType();
                        Map<String, String> headersMap = gson.fromJson(new String(acqsSchedMap.getSchedApiHeaders()), type);

                        for (Map.Entry<String, String> set : headersMap.entrySet()) {
                            headers.set(set.getKey(), set.getValue());
                        }
                    }

                    switch (acqsSchedMap.getSchedApiMethod()){
                        case "GET":
                            HttpEntity requestGet= new HttpEntity( acqsSchedMap.getSchedApiPayload(), headers );

                            log.debug("## GET API Request\n"+acqsSchedMap.getSchedApiEndpoint());
                            runLog = runLog+"## GET API Request\n\t"+acqsSchedMap.getSchedApiEndpoint()+"\n";

                            ResponseEntity<String> triggerAPIGetResponse = restTemplate.exchange(acqsSchedMap.getSchedApiEndpoint(), HttpMethod.GET, requestGet, String.class);

                            log.debug("## GET API Response\n"+triggerAPIGetResponse.getBody());
                            runLog = runLog+"## GET API Response\n\t"+triggerAPIGetResponse.getBody()+"\n";

                            if(triggerAPIGetResponse.getStatusCode().is2xxSuccessful())
                                acqsExecHist.setQuartzExecutionStatus("SUCCESS");
                            else
                                acqsExecHist.setQuartzExecutionStatus("FAILED");
                            if(null!=triggerAPIGetResponse.getBody())
                                acqsExecHist.setQuartzExecutionApiResponsePayload(triggerAPIGetResponse.getBody().getBytes());

                            break;
                        case "POST":
                            HttpEntity requestPost= new HttpEntity( acqsSchedMap.getSchedApiPayload(), headers );

                            log.debug("## POST API Request\n"+requestPost.getBody());
                            runLog = runLog+"## POST API Request\n\t"+requestPost.getBody()+"\n";

                            ResponseEntity<String> triggerAPIPostResponse = restTemplate.exchange(acqsSchedMap.getSchedApiEndpoint(), HttpMethod.POST, requestPost, String.class);

                            log.debug("## POST API Response\n"+triggerAPIPostResponse.getBody());
                            runLog = runLog+"## POST API Response\n\t"+triggerAPIPostResponse.getBody()+"\n";

                            if(triggerAPIPostResponse.getStatusCode().is2xxSuccessful())
                                acqsExecHist.setQuartzExecutionStatus("SUCCESS");
                            else
                                acqsExecHist.setQuartzExecutionStatus("FAILED");
                            if(null!=triggerAPIPostResponse.getBody())
                                acqsExecHist.setQuartzExecutionApiResponsePayload(triggerAPIPostResponse.getBody().getBytes());

                            break;
                        case "PUT":
                            HttpEntity requestPut= new HttpEntity( acqsSchedMap.getSchedApiPayload(), headers );

                            log.debug("## PUT API Request\n"+requestPut.getBody());
                            runLog = runLog+"## PUT API Request\n\t"+requestPut.getBody()+"\n";

                            ResponseEntity<String> triggerAPIPutResponse = restTemplate.exchange(acqsSchedMap.getSchedApiEndpoint(), HttpMethod.PUT, requestPut, String.class);

                            log.debug("## PUT API Response\n"+triggerAPIPutResponse.getBody());
                            runLog = runLog+"## PUT API Response\n\t"+triggerAPIPutResponse.getBody()+"\n";

                            if(triggerAPIPutResponse.getStatusCode().is2xxSuccessful())
                                acqsExecHist.setQuartzExecutionStatus("SUCCESS");
                            else
                                acqsExecHist.setQuartzExecutionStatus("FAILED");
                            if(null!=triggerAPIPutResponse.getBody())
                                acqsExecHist.setQuartzExecutionApiResponsePayload(triggerAPIPutResponse.getBody().getBytes());

                            break;
                        case "PATCH":
                            HttpEntity requestPatch= new HttpEntity( acqsSchedMap.getSchedApiPayload(), headers );

                            log.debug("## PATCH API Request\n"+requestPatch.getBody());
                            runLog = runLog+"## PATCH API Request\n\t"+requestPatch.getBody()+"\n";

                            ResponseEntity<String> triggerAPIPatchResponse = restTemplate.exchange(acqsSchedMap.getSchedApiEndpoint(), HttpMethod.PATCH, requestPatch, String.class);

                            log.debug("## PATCH API Response\n"+triggerAPIPatchResponse.getBody());
                            runLog = runLog+"## PATCH API Response\n\t"+triggerAPIPatchResponse.getBody()+"\n";

                            if(triggerAPIPatchResponse.getStatusCode().is2xxSuccessful())
                                acqsExecHist.setQuartzExecutionStatus("SUCCESS");
                            else
                                acqsExecHist.setQuartzExecutionStatus("FAILED");
                            if(null!=triggerAPIPatchResponse.getBody())
                                acqsExecHist.setQuartzExecutionApiResponsePayload(triggerAPIPatchResponse.getBody().getBytes());

                            break;
                        case "DELETE":
                            HttpEntity requestDelete= new HttpEntity( acqsSchedMap.getSchedApiPayload(), headers );

                            log.debug("## DELETE API Request\n"+requestDelete.getBody());
                            runLog = runLog+"## DELETE API Request\n\t"+requestDelete.getBody()+"\n";

                            ResponseEntity<String> triggerAPIDeleteResponse = restTemplate.exchange(acqsSchedMap.getSchedApiEndpoint(), HttpMethod.DELETE, requestDelete, String.class);

                            log.debug("## DELETE API Response\n"+triggerAPIDeleteResponse.getBody());
                            runLog = runLog+"## DELETE API Request\n\t"+triggerAPIDeleteResponse.getBody()+"\n";

                            if(triggerAPIDeleteResponse.getStatusCode().is2xxSuccessful())
                                acqsExecHist.setQuartzExecutionStatus("SUCCESS");
                            else
                                acqsExecHist.setQuartzExecutionStatus("FAILED");
                            if(null!=triggerAPIDeleteResponse.getBody())
                                acqsExecHist.setQuartzExecutionApiResponsePayload(triggerAPIDeleteResponse.getBody().getBytes());

                            break;
                        default:
                            runLog = runLog+"Invalid HTTP Method / Not Supported";
                            acqsExecHist.setQuartzExecutionStatus("SKIPPED");
                            break;
                    }

                    acqsExecHist.setQuartzExecutionLog(runLog);

                    if(isAuthFailed && acqsExecHist.getQuartzExecutionStatus().equalsIgnoreCase("SUCCESS"))
                        acqsExecHist.setQuartzExecutionStatus("PARTIAL SUCCESS");

                    acqsExecHist = this.acqsExecHistRepository.saveAndFlush(acqsExecHist);

                } catch(HttpClientErrorException e) {

                    if(e.getStatusCode().compareTo(HttpStatus.CONFLICT) == 0)
                        log.warn("HTTP ERROR Executing Job {} : {}", jobExecutionContext.getJobDetail().getKey(),e.getMessage());
                    else
                        log.error("HTTP ERROR Executing Job {} : {}", jobExecutionContext.getJobDetail().getKey(),e.getMessage());
                    runLog = runLog+e.getMessage();

                    acqsExecHist.setQuartzExecutionStatus("FAILED");
                    acqsExecHist.setQuartzExecutionLog(runLog);

                    acqsExecHist = this.acqsExecHistRepository.saveAndFlush(acqsExecHist);
                }
                catch (Exception e){

                    log.error("Unhandled ERROR Executing Job {} : {}", jobExecutionContext.getJobDetail().getKey(),e.getMessage());

                    runLog = runLog+e.getMessage();

                    acqsExecHist.setQuartzExecutionStatus("FATAL");
                    acqsExecHist.setQuartzExecutionLog(runLog);

                    acqsExecHist = this.acqsExecHistRepository.saveAndFlush(acqsExecHist);
                }
            }
            else {
                log.error("No Schedule Details Found in Database for Mapping Id: " + mappingId + " !!!");
                runLog = runLog + "No Schedule Details Found in Database for Mapping Id: " + mappingId + " !!!\n";
            }
        }
        else {
            log.error("Invalid Mapping ID Found in Job Data Map !!!");
            runLog = runLog + "Invalid Mapping ID Found in Job Data Map !!!\n";
        }
    }
}
