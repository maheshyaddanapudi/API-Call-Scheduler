package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.mappers;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedMap;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedOAuth2Map;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.ScheduleDetails;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.ScheduleOAuth2DetailsComponent;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.buffer.ScheduleDetailsBuffer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {

    ScheduleDetailsBuffer mapScheduleToDto(AcqsSchedMap schedule);

    AcqsSchedMap mapDtoToSchedule(ScheduleDetailsBuffer scheduleDetails);

    ScheduleOAuth2DetailsComponent mapScheduleOAuth2DetailsComponentToDto(AcqsSchedOAuth2Map scheduleOAuth2Map);

    AcqsSchedOAuth2Map mapDtoToScheduleOAuth2DetailsComponent(ScheduleOAuth2DetailsComponent scheduleOAuth2DetailsComponent);
}