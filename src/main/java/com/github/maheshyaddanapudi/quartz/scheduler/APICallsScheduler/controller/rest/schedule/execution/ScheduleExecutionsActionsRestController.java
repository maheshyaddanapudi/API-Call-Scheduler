package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.controller.rest.schedule.execution;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsExecHist;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.ExecutionDetails;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.embedded.oauth2.BaseResponseDTO;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.APICallQuartzExecutionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@SuppressWarnings({ "unchecked", "rawtypes" })
@Tag(name = "API Call Quartz Scheduler Execution", description = "The API provides the interface for view or delete an execution.")
@CrossOrigin("*")
@RequestMapping(value = "/api/execution", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleExecutionsActionsRestController {

    @Autowired
    APICallQuartzExecutionHistoryService apiCallQuartzExecutionHistoryService;

    @GetMapping(produces = "application/json")
    @Operation(summary = "Provides All Schedules Info", description = "Returns JSON formatted All Executions Info", tags = { "executions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned All Executions Info",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExecutionDetails.class)))) ,
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExecutionDetails.class))))})
    @ResponseBody
    public ResponseEntity<List<ExecutionDetails>> getAllExecutions(){
        try{
            List<ExecutionDetails> executions = new ArrayList<ExecutionDetails>();
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

            this.apiCallQuartzExecutionHistoryService.findAll().stream().forEach((execution) -> {
                ExecutionDetails anExecutionDetails = new ExecutionDetails(execution.getQrtzExecId(), execution.getQuartzExecutionStatus(), execution.getQuartzExecutionLog(), dateFormat.format(execution.getInsertTimestamp()), dateFormat.format(execution.getUpdateTimestamp()));
                if(null!=execution.getQuartzExecutionApiResponsePayload())
                    anExecutionDetails.setQuartzExecutionApiResponsePayload(new String(execution.getQuartzExecutionApiResponsePayload()));
                if(null!=execution.getAcqsSchedMap().getSchedApiPayload())
                    anExecutionDetails.setQuartzExecutionApiResquestPayload(new String(execution.getAcqsSchedMap().getSchedApiPayload()));
                executions.add(anExecutionDetails);
            });

            return new ResponseEntity(executions, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(new ArrayList<ExecutionDetails>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value ="/schedule/{scheduleId}", produces = "application/json")
    @Operation(summary = "Provides Executions Info for a given Schedule", description = "Returns JSON formatted Executions Info", tags = { "executions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned Executions Info",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExecutionDetails.class)))) ,
            @ApiResponse(responseCode = "404", description = "Schedule Id Not Found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExecutionDetails.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExecutionDetails.class))))})
    @ResponseBody
    public ResponseEntity<List<ExecutionDetails>> getExecutionsForSchedule(@Valid @PathVariable String scheduleId){
        try{
            List<ExecutionDetails> executions = new ArrayList<ExecutionDetails>();
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

            this.apiCallQuartzExecutionHistoryService.findAllByScheduleId(scheduleId).stream().forEach((execution) -> {
                ExecutionDetails anExecutionDetails = new ExecutionDetails(execution.getQrtzExecId(), execution.getQuartzExecutionStatus(), execution.getQuartzExecutionLog(), dateFormat.format(execution.getInsertTimestamp()), dateFormat.format(execution.getUpdateTimestamp()));
                if(null!=execution.getQuartzExecutionApiResponsePayload())
                    anExecutionDetails.setQuartzExecutionApiResponsePayload(new String(execution.getQuartzExecutionApiResponsePayload()));
                if(null!=execution.getAcqsSchedMap().getSchedApiPayload())
                    anExecutionDetails.setQuartzExecutionApiResquestPayload(new String(execution.getAcqsSchedMap().getSchedApiPayload()));
                executions.add(anExecutionDetails);
            });

             return executions.isEmpty() ? new ResponseEntity(executions, HttpStatus.NOT_FOUND) : new ResponseEntity(executions, HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(new ArrayList<ExecutionDetails>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value ="/{executionId}", produces = "application/json")
    @Operation(summary = "Provides Executions Info for a given Execution", description = "Returns JSON formatted Executions Info", tags = { "executions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned Execution Info",
                    content = @Content(schema = @Schema(implementation = ExecutionDetails.class))) ,
            @ApiResponse(responseCode = "404", description = "Execution Id Not Found", content = @Content(schema = @Schema(implementation = ExecutionDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content(schema = @Schema(implementation = ExecutionDetails.class)))})
    @ResponseBody
    public ResponseEntity<ExecutionDetails> getExecution(@Valid @PathVariable String executionId){
        try{
            List<ExecutionDetails> executions = new ArrayList<ExecutionDetails>();
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

            AcqsExecHist execution = this.apiCallQuartzExecutionHistoryService.findByExecutionId(executionId);

            if(null!=execution)
            {
                ExecutionDetails anExecutionDetails = new ExecutionDetails(execution.getQrtzExecId(), execution.getQuartzExecutionStatus(), execution.getQuartzExecutionLog(), dateFormat.format(execution.getInsertTimestamp()), dateFormat.format(execution.getUpdateTimestamp()));
                if(null!=execution.getQuartzExecutionApiResponsePayload())
                    anExecutionDetails.setQuartzExecutionApiResponsePayload(new String(execution.getQuartzExecutionApiResponsePayload()));
                if(null!=execution.getAcqsSchedMap().getSchedApiPayload())
                    anExecutionDetails.setQuartzExecutionApiResquestPayload(new String(execution.getAcqsSchedMap().getSchedApiPayload()));

                return new ResponseEntity(anExecutionDetails, HttpStatus.OK);
            }
            else
                return new ResponseEntity(new ExecutionDetails(), HttpStatus.NOT_FOUND);

        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(new ExecutionDetails(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value ="/{executionId}", produces = "application/json")
    @Operation(summary = "Deletes Execution Info for a given Execution", description = "Returns JSON formatted Executions Info", tags = { "executions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted Execution Info",
                    content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
            @ApiResponse(responseCode = "404", description = "Execution Id Not Found", content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
            @ApiResponse(responseCode = "406", description = "Execution Id is still running and cannot be deleted", content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))})
    @ResponseBody
    public ResponseEntity<BaseResponseDTO> deleteExecution(@Valid @PathVariable String executionId){
        try{
            BaseResponseDTO response = this.apiCallQuartzExecutionHistoryService.deleteByExecutionId(executionId);
            if(response.isStatus())
                return new ResponseEntity(response, HttpStatus.OK);
            else {
                if(response.getMessage().equalsIgnoreCase(executionId+" Not found !!!"))
                    return new ResponseEntity(response, HttpStatus.NOT_FOUND);
                else
                    return new ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(new BaseResponseDTO(false, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value ="/schedule/{scheduleId}", produces = "application/json")
    @Operation(summary = "Deletes Execution Info for a given Schedule", description = "Returns JSON formatted Executions Info", tags = { "executions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted Executions for the schedule",
                    content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))) ,
            @ApiResponse(responseCode = "404", description = "Schedule Id Not Found", content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content(schema = @Schema(implementation = BaseResponseDTO.class)))})
    @ResponseBody
    public ResponseEntity<BaseResponseDTO> deleteExecutionsForSchedule(@Valid @PathVariable String scheduleId){
        try{
            BaseResponseDTO response = this.apiCallQuartzExecutionHistoryService.deleteAllByScheduleId(scheduleId);
            if(response.isStatus())
                return new ResponseEntity(response, HttpStatus.OK);
            else {
                if(response.getMessage().equalsIgnoreCase(scheduleId+" Not Found !!!"))
                    return new ResponseEntity(response, HttpStatus.NOT_FOUND);
                else
                    return new ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(new BaseResponseDTO(false, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
