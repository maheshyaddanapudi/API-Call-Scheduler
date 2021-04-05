package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.controller.rest.schedule.metadata;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.config.quartz.QuartzConfig;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedMap;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.request.schedule.ScheduleRequest;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.ScheduleDetails;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.ScheduleOAuth2DetailsComponent;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.buffer.ScheduleDetailsBuffer;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.embedded.oauth2.BaseResponseDTO;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.mappers.ScheduleMapper;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.APICallQuartzSchedulerService;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@SuppressWarnings({ "unchecked", "rawtypes" })
@Tag(name = "API Call Quartz Scheduler Metadata", description = "The API provides the interface for create / update or delete a schedule.")
@CrossOrigin("*")
@RequestMapping(value = "/api/metadata/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ScheduleActionsRestController {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private APICallQuartzSchedulerService apiCallQuartzSchedulerService;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private Gson gson;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Provides All Schedules Info", description = "Returns JSON formatted All Schedules Info", tags = { "metadata" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned All Schedules Info",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScheduleDetails.class)))) ,
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
    @ResponseBody
    public ResponseEntity<List<ScheduleDetails>> getAllSchedules(){
        try{
            List<ScheduleDetails> schedules = new ArrayList<ScheduleDetails>();
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
            this.apiCallQuartzSchedulerService.findAllSchedules().stream().forEach((schedule) -> {
                ScheduleDetailsBuffer scheduleDetailsBuffer = this.scheduleMapper.mapScheduleToDto(schedule);

                ScheduleDetails scheduleDetails = new ScheduleDetails(scheduleDetailsBuffer.getSchedApiEndpoint(), scheduleDetailsBuffer.getSchedApiMethod(), scheduleDetailsBuffer.getSchedName(), scheduleDetailsBuffer.getSchedDesc(), scheduleDetailsBuffer.getSchedCronExpression(), scheduleDetailsBuffer.getSchedCurrentStatus(), null != scheduleDetailsBuffer.getSchedStartTimestamp() ? dateFormat.format(scheduleDetailsBuffer.getSchedStartTimestamp()) : null, dateFormat.format(scheduleDetailsBuffer.getSchedStopTimestamp()));
                scheduleDetails.setScheduleId(scheduleDetailsBuffer.getQrtzSchedId());
                if(null!=scheduleDetailsBuffer.getSchedApiPayload())
                    scheduleDetails.setApiPayload(new String(scheduleDetailsBuffer.getSchedApiPayload()));

                ScheduleOAuth2DetailsComponent scheduleOAuth2DetailsComponent = this.scheduleMapper.mapScheduleOAuth2DetailsComponentToDto(this.apiCallQuartzSchedulerService.findSchedOAuth2Map(schedule));
                if(null!=scheduleOAuth2DetailsComponent)
                    scheduleDetails.setApiOAuth2Metadata(scheduleOAuth2DetailsComponent);
                schedules.add(scheduleDetails);
            });
            return new ResponseEntity(schedules, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(new ArrayList<ScheduleDetails>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{scheduleId}", produces = "application/json")
    @Operation(summary = "Provides Schedule Info", description = "Returns JSON formatted Schedule Info", tags = { "metadata" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned Schedule Info",
                    content = @Content(schema = @Schema(implementation = ScheduleDetails.class))) ,
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
    @ResponseBody
    public ResponseEntity<ScheduleDetails> getSchedule(@Valid @PathVariable String scheduleId){
        try{

            AcqsSchedMap acqsSchedMap = this.apiCallQuartzSchedulerService.findSchedule(scheduleId);
            ScheduleDetailsBuffer scheduleDetailsBuffer = this.scheduleMapper.mapScheduleToDto(acqsSchedMap);

            if(null!=scheduleDetailsBuffer)
            {
                DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
                ScheduleDetails scheduleDetails = new ScheduleDetails(scheduleDetailsBuffer.getSchedApiEndpoint(), scheduleDetailsBuffer.getSchedApiMethod(), scheduleDetailsBuffer.getSchedName(), scheduleDetailsBuffer.getSchedDesc(), scheduleDetailsBuffer.getSchedCronExpression(), scheduleDetailsBuffer.getSchedCurrentStatus(), null!=scheduleDetailsBuffer.getSchedStartTimestamp() ? dateFormat.format(scheduleDetailsBuffer.getSchedStartTimestamp()) : null, dateFormat.format(scheduleDetailsBuffer.getSchedStopTimestamp()));
                scheduleDetails.setScheduleId(scheduleDetailsBuffer.getQrtzSchedId());
                if(null!=scheduleDetailsBuffer.getSchedApiPayload())
                    scheduleDetails.setApiPayload(new String(scheduleDetailsBuffer.getSchedApiPayload()));

                ScheduleOAuth2DetailsComponent scheduleOAuth2DetailsComponent = this.scheduleMapper.mapScheduleOAuth2DetailsComponentToDto(this.apiCallQuartzSchedulerService.findSchedOAuth2Map(acqsSchedMap));
                if(null!=scheduleOAuth2DetailsComponent)
                    scheduleDetails.setApiOAuth2Metadata(scheduleOAuth2DetailsComponent);

                return new ResponseEntity(scheduleDetails, HttpStatus.OK);
            }
            else
                return new ResponseEntity(new ScheduleDetails(), HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Add new Schedule i.e. onboard with generic API Details to which the Call has to be performed", description = "Takes in the new schedule details like schedule name, start & end times, cron expression, payload etc. Returns a status of the action with a message.", tags = { "metadata" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully onboarded new Schedule and persisted in Database",
                    content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
            @ApiResponse(responseCode = "400", description = "Bad Request with reason", content = @Content()),
            @ApiResponse(responseCode = "406", description = "Duplicate Schedule Name Found.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<BaseResponseDTO> scheduleExecution(@Valid @RequestBody ScheduleRequest request) {

        try {
            String uniqueIdentifier = UUID.randomUUID().toString();

            BaseResponseDTO scheduleCreationResponse = this.apiCallQuartzSchedulerService.addNewSchedule(request, uniqueIdentifier);

            if(scheduleCreationResponse.isStatus()){

                JobDetail jobDetail = QuartzConfig.buildJobDetail(Long.parseLong(scheduleCreationResponse.getMessage()), uniqueIdentifier);
                Trigger trigger = QuartzConfig.buildJobTrigger(request.getStartDate(), request.getEndDate(), jobDetail, request.getCronExpression());
                this.schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);

                this.apiCallQuartzSchedulerService.updateScheduleStatus(Long.parseLong(scheduleCreationResponse.getMessage()), "SCHEDULED");

                BaseResponseDTO baseResponseDTO = new BaseResponseDTO(true, uniqueIdentifier);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            }
            else
            {
                return scheduleCreationResponse.getMessage().equalsIgnoreCase("Duplicate Schedule Found!!!") ? new ResponseEntity<>(scheduleCreationResponse, HttpStatus.NOT_ACCEPTABLE) : new ResponseEntity<>(scheduleCreationResponse, HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e) {

            e.printStackTrace();

            log.error("scheduleExecution Error: "+e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Pause a schedule.", description = "Takes in the scheduleId. Returns a status of the action with a message.", tags = { "metadata" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully paused schedule",
                    content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
            @ApiResponse(responseCode = "404", description = "Schedule Id Not Found", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
    @PatchMapping(value = "/pause/{scheduleId}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<BaseResponseDTO> pauseExecution(@PathVariable String scheduleId){

        try {

            if(this.apiCallQuartzSchedulerService.pauseSchedule(scheduleId))
            {
                this.schedulerFactoryBean.getScheduler().pauseJob(new JobKey(scheduleId, Constants.API));
                BaseResponseDTO baseResponseDTO = new BaseResponseDTO(true, scheduleId);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            }
            else
            {
                BaseResponseDTO baseResponseDTO = new BaseResponseDTO(false, scheduleId+" Not Found !!!");
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }

        } catch (SchedulerException e) {
            log.error("pauseExecution Error: "+e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Resume a schedule.", description = "Takes in the scheduleId. Returns a status of the action with a message.", tags = { "metadata" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully resumed schedule",
                    content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
            @ApiResponse(responseCode = "404", description = "Schedule Id Not Found", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
    @PatchMapping(value = "/resume/{scheduleId}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<BaseResponseDTO> resumeExecution(@PathVariable String scheduleId){

        try {

            if(this.apiCallQuartzSchedulerService.resumeSchedule(scheduleId))
            {
                this.schedulerFactoryBean.getScheduler().resumeJob(new JobKey(scheduleId, Constants.API));
                BaseResponseDTO baseResponseDTO = new BaseResponseDTO(true, scheduleId);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            }
            else
            {
                BaseResponseDTO baseResponseDTO = new BaseResponseDTO(false, scheduleId+" Not Found !!!");
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }

        } catch (SchedulerException e) {
            log.error("resumeExecution Error: "+e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Delete a schedule.", description = "Takes in the scheduleId. Returns a status of the action with a message.", tags = { "metadata" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted schedule",
                    content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
            @ApiResponse(responseCode = "404", description = "Schedule Id Not Found", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
    @DeleteMapping(value = "/{scheduleId}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<BaseResponseDTO> deleteExecution(@Valid @PathVariable String scheduleId){

        try {

            if(this.apiCallQuartzSchedulerService.deleteSchedule(scheduleId))
            {
                this.schedulerFactoryBean.getScheduler().deleteJob(new JobKey(scheduleId, Constants.API));
                BaseResponseDTO baseResponseDTO = new BaseResponseDTO(true, scheduleId);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            }
            else
            {
                BaseResponseDTO baseResponseDTO = new BaseResponseDTO(false, scheduleId+" Not Found !!!");
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }

        } catch (SchedulerException e) {
            log.error("deleteExecution Error: "+e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Update Schedule i.e.  with generic API Details to which the Call has to be performed", description = "Takes in the new schedule details like schedule name, start & end times, cron expression, payload etc. Returns a status of the action with a message.", tags = { "metadata" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Schedule and persisted in Database",
                    content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
            @ApiResponse(responseCode = "400", description = "Bad Request with reason", content = @Content()),
            @ApiResponse(responseCode = "406", description = "Schedule Not Found.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
    @PutMapping(value = "/{scheduleId}", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<BaseResponseDTO> updateExecution(@Valid @PathVariable String scheduleId, @Valid @RequestBody ScheduleRequest request) {

        try {
            BaseResponseDTO scheduleUpdationResponse = this.apiCallQuartzSchedulerService.updateSchedule(request, scheduleId);

            if(scheduleUpdationResponse.isStatus()){

                boolean isJobTriggerDeleted = this.schedulerFactoryBean.getScheduler().deleteJob(new JobKey(scheduleId, Constants.API));

                JobDetail jobDetail = QuartzConfig.buildJobDetail(Long.parseLong(scheduleUpdationResponse.getMessage()), scheduleId);
                Trigger trigger = QuartzConfig.buildJobTrigger(request.getStartDate(), request.getEndDate(), jobDetail, request.getCronExpression());
                this.schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);

                this.apiCallQuartzSchedulerService.updateScheduleStatus(Long.parseLong(scheduleUpdationResponse.getMessage()), "SCHEDULED");

                BaseResponseDTO baseResponseDTO = new BaseResponseDTO(true, scheduleId);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            }
            else
            {
                return scheduleUpdationResponse.getMessage().equalsIgnoreCase("Schedule Not Found!!!") ? new ResponseEntity<>(scheduleUpdationResponse, HttpStatus.NOT_ACCEPTABLE) : new ResponseEntity<>(scheduleUpdationResponse, HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e) {

            log.error("updateExecution Error: "+e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
