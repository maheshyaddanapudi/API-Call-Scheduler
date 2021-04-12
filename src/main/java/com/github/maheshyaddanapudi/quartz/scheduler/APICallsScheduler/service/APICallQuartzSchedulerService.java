package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedMap;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedOAuth2Map;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository.AcqsSchedMapRepository;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository.AcqsSchedOAuth2MapRepository;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.request.schedule.ScheduleRequest;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.embedded.oauth2.BaseResponseDTO;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.internal.cipher.EncryptionDecryptionService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;

@Service
public class APICallQuartzSchedulerService {

    @Autowired
    private AcqsSchedMapRepository acqsSchedMapRepository;

    @Autowired
    private AcqsSchedOAuth2MapRepository acqsSchedOAuth2MapRepository;

    @Autowired
    private EncryptionDecryptionService encryptionDecryptionService;

    public List<AcqsSchedMap> findAllSchedules(){
        return this.acqsSchedMapRepository.findAll();
    }

    public AcqsSchedMap findSchedule(String scheduleId){
        return this.acqsSchedMapRepository.findByQrtzSchedId(scheduleId);
    }

    public AcqsSchedOAuth2Map findSchedOAuth2Map(AcqsSchedMap acqsSchedMap){
        return this.acqsSchedOAuth2MapRepository.findByAcqsSchedMap(acqsSchedMap);
    }

    public BaseResponseDTO addNewSchedule(ScheduleRequest request, String uniqueIdentifier) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {

        if(isDuplicate(request.getScheduleName()))
        {
            return new BaseResponseDTO(false, "Duplicate Schedule Found!!!");
        }
        else
        {
            DateFormat dafeFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

            try{
                AcqsSchedMap acqsSchedMap = new AcqsSchedMap();

                acqsSchedMap.setQrtzSchedId(uniqueIdentifier);
                acqsSchedMap.setSchedName(request.getScheduleName());
                acqsSchedMap.setSchedDesc(request.getScheduleDesc());
                acqsSchedMap.setSchedCurrentStatus("DRAFT");
                acqsSchedMap.setSchedApiEndpoint(request.getApiEndPoint());
                acqsSchedMap.setSchedApiMethod(request.getApiMethod());
                if(null!=request.getApiPayload())
                    acqsSchedMap.setSchedApiPayload(request.getApiPayload().getBytes());
                acqsSchedMap.setSchedCronExpression(request.getCronExpression());
                if(null!=request.getStartDate())
                    acqsSchedMap.setSchedStartTimestamp(dafeFormat.parse(request.getStartDate()));
                acqsSchedMap.setSchedStopTimestamp(dafeFormat.parse(request.getEndDate()));

                if(null!=request.getApiHeaders() && !request.getApiHeaders().isEmpty())
                {
                    Gson gson = new Gson();

                    acqsSchedMap.setSchedApiHeaders(gson.toJson(request.getApiHeaders()).getBytes());
                }

                acqsSchedMap = this.acqsSchedMapRepository.saveAndFlush(acqsSchedMap);
                if(null!=acqsSchedMap && acqsSchedMap.getMappingId() > 0) {

                    if(null!=request.getApiOAuth2Metadata())
                    {
                        AcqsSchedOAuth2Map acqsSchedOAuth2Map = new AcqsSchedOAuth2Map();

                        acqsSchedOAuth2Map.setOauth2TokenUrl(request.getApiOAuth2Metadata().getOauth2TokenUrl());

                        if(null!=request.getApiOAuth2Metadata().getOauth2TokenUrl())
                            acqsSchedOAuth2Map.setOauth2UserInfoUrl(request.getApiOAuth2Metadata().getOauth2UserInfoUrl());

                        acqsSchedOAuth2Map.setAcqsSchedMap(acqsSchedMap);
                        acqsSchedOAuth2Map.setOauth2ClientId(request.getApiOAuth2Metadata().getClientId());
                        acqsSchedOAuth2Map.setOauth2ClientSecretEncrypted(this.encryptionDecryptionService.encrypt(
                                new String(Base64.getDecoder()
                                    .decode(
                                            request.getApiOAuth2Metadata().getClientSecretBase64()
                                            )
                                        )
                                    )
                                );
                        acqsSchedOAuth2Map.setOauth2GrantType(null!=request.getApiOAuth2Metadata().getGrantType() ? request.getApiOAuth2Metadata().getGrantType() : "password");
                        acqsSchedOAuth2Map.setOauth2Username(request.getApiOAuth2Metadata().getUsername());
                        acqsSchedOAuth2Map.setOauth2PasswordEncrypted(this.encryptionDecryptionService.encrypt(
                                new String(Base64.getDecoder()
                                        .decode(
                                                request.getApiOAuth2Metadata().getPasswordBase64()
                                                )
                                            )
                                        )
                                    );

                        acqsSchedOAuth2Map = this.acqsSchedOAuth2MapRepository.saveAndFlush(acqsSchedOAuth2Map);

                        if(null!=acqsSchedOAuth2Map && acqsSchedOAuth2Map.getAuthMappingId() > 0)
                            return new BaseResponseDTO(true, String.valueOf(acqsSchedMap.getMappingId()));
                        else
                        {
                            this.acqsSchedMapRepository.delete(acqsSchedMap);
                            return new BaseResponseDTO(false, "Error while persisting OAuth2 Metadata to Database!!!");
                        }
                    }
                    else
                        return new BaseResponseDTO(true, String.valueOf(acqsSchedMap.getMappingId()));
                }
                else
                    return new BaseResponseDTO(false, "Error while persisting Schedule Metadata to Database!!!");
            }
            catch (ParseException parseException){
                return new BaseResponseDTO(false, "Invalid Date format - Accepted Format is "+Constants.DATE_FORMAT+"!!!");
            }

        }
    }

    public boolean updateScheduleStatus(long mappingId, String status){
        AcqsSchedMap acqsSchedMap = this.acqsSchedMapRepository.findByMappingId(mappingId);

        if(null!=acqsSchedMap && acqsSchedMap.getMappingId() > 0)
        {
            acqsSchedMap.setSchedCurrentStatus(status);
            this.acqsSchedMapRepository.saveAndFlush(acqsSchedMap);
            return true;
        }
        else
            return false;
    }

    public boolean pauseSchedule(String qrtzSchedId){
        AcqsSchedMap acqsSchedMap = this.acqsSchedMapRepository.findByQrtzSchedId(qrtzSchedId);

        if(null!=acqsSchedMap && acqsSchedMap.getMappingId() > 0)
        {
            acqsSchedMap.setSchedCurrentStatus("PAUSED");
            this.acqsSchedMapRepository.saveAndFlush(acqsSchedMap);
            return true;
        }
        else
            return false;
    }

    public boolean resumeSchedule(String qrtzSchedId){
        AcqsSchedMap acqsSchedMap = this.acqsSchedMapRepository.findByQrtzSchedId(qrtzSchedId);

        if(null!=acqsSchedMap && acqsSchedMap.getMappingId() > 0)
        {
            acqsSchedMap.setSchedCurrentStatus("SCHEDULED");
            this.acqsSchedMapRepository.saveAndFlush(acqsSchedMap);
            return true;
        }
        else
            return false;
    }

    public boolean deleteSchedule(String qrtzSchedId){
        AcqsSchedMap acqsSchedMap = this.acqsSchedMapRepository.findByQrtzSchedId(qrtzSchedId);

        if(null!=acqsSchedMap && acqsSchedMap.getMappingId() > 0)
        {
            this.acqsSchedMapRepository.delete(acqsSchedMap);
            return true;
        }
        else
            return false;
    }


    public BaseResponseDTO updateSchedule(ScheduleRequest request, String uniqueIdentifier) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {

        AcqsSchedMap acqsSchedMap = this.acqsSchedMapRepository.findByQrtzSchedId(uniqueIdentifier);

        if(null == acqsSchedMap || acqsSchedMap.getMappingId() < 1)
        {
            return new BaseResponseDTO(false, "Schedule Not Found!!!");
        }
        else
        {
            DateFormat dafeFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

            try{
                acqsSchedMap.setSchedDesc(request.getScheduleDesc());
                acqsSchedMap.setSchedCurrentStatus("DRAFT");
                acqsSchedMap.setSchedApiEndpoint(request.getApiEndPoint());
                acqsSchedMap.setSchedApiMethod(request.getApiMethod());
                if(null!=request.getApiPayload())
                    acqsSchedMap.setSchedApiPayload(request.getApiPayload().getBytes());
                acqsSchedMap.setSchedCronExpression(request.getCronExpression());
                if(null!=request.getStartDate())
                    acqsSchedMap.setSchedStartTimestamp(dafeFormat.parse(request.getStartDate()));
                acqsSchedMap.setSchedStopTimestamp(dafeFormat.parse(request.getEndDate()));

                if(null!=request.getApiHeaders() && !request.getApiHeaders().isEmpty())
                {
                    Gson gson = new Gson();

                    acqsSchedMap.setSchedApiHeaders(gson.toJson(request.getApiHeaders()).getBytes());
                }

                acqsSchedMap = this.acqsSchedMapRepository.saveAndFlush(acqsSchedMap);
                if(null!=acqsSchedMap && acqsSchedMap.getMappingId() > 0) {

                    AcqsSchedOAuth2Map acqsSchedOAuth2Map = this.acqsSchedOAuth2MapRepository.findByAcqsSchedMap(acqsSchedMap);

                    if(null!=request.getApiOAuth2Metadata())
                    {
                        if((null==acqsSchedOAuth2Map || acqsSchedOAuth2Map.getAuthMappingId() < 1)){
                            acqsSchedOAuth2Map = new AcqsSchedOAuth2Map();
                        }

                        acqsSchedOAuth2Map.setOauth2TokenUrl(request.getApiOAuth2Metadata().getOauth2TokenUrl());

                        if(null!=request.getApiOAuth2Metadata().getOauth2TokenUrl())
                            acqsSchedOAuth2Map.setOauth2UserInfoUrl(request.getApiOAuth2Metadata().getOauth2UserInfoUrl());

                        acqsSchedOAuth2Map.setAcqsSchedMap(acqsSchedMap);
                        acqsSchedOAuth2Map.setOauth2ClientId(request.getApiOAuth2Metadata().getClientId());
                        acqsSchedOAuth2Map.setOauth2ClientSecretEncrypted(this.encryptionDecryptionService.encrypt(
                                new String(Base64.getDecoder()
                                        .decode(
                                                request.getApiOAuth2Metadata().getClientSecretBase64()
                                        )
                                )
                                )
                        );
                        acqsSchedOAuth2Map.setOauth2GrantType(null!=request.getApiOAuth2Metadata().getGrantType() ? request.getApiOAuth2Metadata().getGrantType() : "password");
                        acqsSchedOAuth2Map.setOauth2Username(request.getApiOAuth2Metadata().getUsername());
                        acqsSchedOAuth2Map.setOauth2PasswordEncrypted(this.encryptionDecryptionService.encrypt(
                                new String(Base64.getDecoder()
                                        .decode(
                                                request.getApiOAuth2Metadata().getPasswordBase64()
                                        )
                                )
                                )
                        );

                        acqsSchedOAuth2Map = this.acqsSchedOAuth2MapRepository.saveAndFlush(acqsSchedOAuth2Map);

                        if(null!=acqsSchedOAuth2Map && acqsSchedOAuth2Map.getAuthMappingId() > 0)
                            return new BaseResponseDTO(true, String.valueOf(acqsSchedMap.getMappingId()));
                        else
                        {
                            return new BaseResponseDTO(false, "Error while persisting OAuth2 Metadata to Database!!!");
                        }
                    }
                    else {
                        if(null!=acqsSchedOAuth2Map && acqsSchedOAuth2Map.getAuthMappingId() > 0)
                        {
                            this.acqsSchedOAuth2MapRepository.delete(acqsSchedOAuth2Map);
                        }
                        return new BaseResponseDTO(true, String.valueOf(acqsSchedMap.getMappingId()));
                    }
                }
                else
                    return new BaseResponseDTO(false, "Error while persisting Schedule Metadata to Database!!!");
            }
            catch (ParseException parseException){
                return new BaseResponseDTO(false, "Invalid Date format - Accepted Format is "+Constants.DATE_FORMAT+"!!!");
            }

        }
    }


    private boolean isDuplicate(String schedName){
        AcqsSchedMap acqsSchedMap =  this.acqsSchedMapRepository.findBySchedName(schedName);

        if(null!=acqsSchedMap && acqsSchedMap.getMappingId() > 0)
            return true;
        else return false;
    }
}
